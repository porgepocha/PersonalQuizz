package com.jorge.constanca.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.constanca.model.MusicTasteSnapshot;
import com.jorge.constanca.model.SpotifyTrack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SpotifyPkceClient {

    private static final String AUTHORIZE_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String TOP_ARTISTS_URL = "https://api.spotify.com/v1/me/top/artists?limit=5&time_range=medium_term";
    private static final String TOP_TRACKS_URL = "https://api.spotify.com/v1/me/top/tracks?limit=5&time_range=medium_term";
    private static final String SCOPE = "user-top-read";

    private final ObjectMapper objectMapper;
    private final SecureRandom secureRandom;

    public SpotifyPkceClient() {
        this.objectMapper = new ObjectMapper();
        this.secureRandom = new SecureRandom();
    }

    public MusicTasteSnapshot connectAndFetch(Consumer<String> browserOpener) throws Exception {
        if (ClientConfig.SPOTIFY_CLIENT_ID.isBlank()) {
            throw new IllegalStateException("Falta configurar o Spotify.");
        }

        String codeVerifier = generateCodeVerifier();
        String codeChallenge = createCodeChallenge(codeVerifier);
        String state = randomToken(24);

        CompletableFuture<Map<String, String>> callbackFuture = new CompletableFuture<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 43821), 0);
        server.createContext("/callback", exchange -> handleCallback(exchange, callbackFuture));
        server.start();

        try {
            String authUrl = AUTHORIZE_URL
                    + "?response_type=code"
                    + "&client_id=" + encode(ClientConfig.SPOTIFY_CLIENT_ID)
                    + "&scope=" + encode(SCOPE)
                    + "&code_challenge_method=S256"
                    + "&code_challenge=" + encode(codeChallenge)
                    + "&redirect_uri=" + encode(ClientConfig.SPOTIFY_REDIRECT_URI)
                    + "&state=" + encode(state);

            browserOpener.accept(authUrl);

            Map<String, String> callbackParams = callbackFuture.get(120, TimeUnit.SECONDS);
            if (callbackParams.containsKey("error")) {
                throw new IllegalStateException("Nao deu para ligar o Spotify.");
            }

            if (!state.equals(callbackParams.get("state"))) {
                throw new IllegalStateException("Nao foi possivel confirmar a ligacao.");
            }

            String code = callbackParams.get("code");
            if (code == null || code.isBlank()) {
                throw new IllegalStateException("A ligacao ao Spotify nao ficou concluida.");
            }

            String accessToken = exchangeCodeForToken(code, codeVerifier);
            return fetchTopItems(accessToken);
        } finally {
            server.stop(0);
        }
    }

    private void handleCallback(HttpExchange exchange, CompletableFuture<Map<String, String>> callbackFuture) throws IOException {
        Map<String, String> params = parseQuery(exchange.getRequestURI().getRawQuery());
        String response = """
                <html>
                  <body style="font-family: sans-serif; padding: 24px;">
                    <h2>Spotify ligado</h2>
                    <p>Ja podes fechar esta janela.</p>
                  </body>
                </html>
                """;

        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }

        callbackFuture.complete(params);
    }

    private String exchangeCodeForToken(String code, String codeVerifier) throws IOException, InterruptedException {
        String body = "client_id=" + encode(ClientConfig.SPOTIFY_CLIENT_ID)
                + "&grant_type=authorization_code"
                + "&code=" + encode(code)
                + "&redirect_uri=" + encode(ClientConfig.SPOTIFY_REDIRECT_URI)
                + "&code_verifier=" + encode(codeVerifier);

        HttpURLConnection connection = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IllegalStateException("Nao deu para terminar a ligacao. Confirma o client id e o redirect.");
        }

        try (InputStream inputStream = connection.getInputStream()) {
            JsonNode json = objectMapper.readTree(inputStream);
            return json.path("access_token").asText();
        }
    }

    private MusicTasteSnapshot fetchTopItems(String accessToken) throws IOException, InterruptedException {
        List<String> artists = fetchNames(TOP_ARTISTS_URL, accessToken);
        List<SpotifyTrack> tracks = fetchTracks(TOP_TRACKS_URL, accessToken);
        return new MusicTasteSnapshot(artists, tracks, "spotify");
    }

    private List<String> fetchNames(String url, String accessToken) throws IOException, InterruptedException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IllegalStateException("Consegui ligar, mas nao deu para ler os dados.");
        }

        try (InputStream inputStream = connection.getInputStream()) {
            JsonNode root = objectMapper.readTree(inputStream);
            List<String> names = new ArrayList<>();
            for (JsonNode item : root.path("items")) {
                String name = item.path("name").asText();
                if (!name.isBlank()) {
                    names.add(name);
                }
            }
            return names;
        }
    }

    private List<SpotifyTrack> fetchTracks(String url, String accessToken) throws IOException, InterruptedException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IllegalStateException("Consegui ligar, mas nao deu para ler os dados.");
        }

        try (InputStream inputStream = connection.getInputStream()) {
            JsonNode root = objectMapper.readTree(inputStream);
            List<SpotifyTrack> tracks = new ArrayList<>();
            for (JsonNode item : root.path("items")) {
                String name = item.path("name").asText();
                String previewUrl = item.path("preview_url").asText("");
                String spotifyUrl = item.path("external_urls").path("spotify").asText("");
                String artist = "";
                JsonNode artistsNode = item.path("artists");
                if (artistsNode.isArray() && !artistsNode.isEmpty()) {
                    artist = artistsNode.get(0).path("name").asText("");
                }
                if (!name.isBlank()) {
                    tracks.add(new SpotifyTrack(name, artist, previewUrl, spotifyUrl));
                }
            }
            return tracks;
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> values = new HashMap<>();
        if (query == null || query.isBlank()) {
            return values;
        }

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            values.put(key, value);
        }
        return values;
    }

    private String generateCodeVerifier() {
        return randomToken(64);
    }

    private String randomToken(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }
        return builder.toString();
    }

    private String createCodeChallenge(String verifier) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(verifier.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    private String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
