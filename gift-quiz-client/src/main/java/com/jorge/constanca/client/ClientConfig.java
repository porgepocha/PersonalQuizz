package com.jorge.constanca.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ClientConfig {

    public static final String CLIENT_VERSION = "1.0-demo";
    public static final String SUBMISSION_URL = "https://spring-boot-production-2870.up.railway.app/api/submissions";
    public static final String SPOTIFY_REDIRECT_URI = "http://127.0.0.1:43821/callback";
    public static final String SPOTIFY_CLIENT_ID = resolveSpotifyClientId();
    public static final Path APP_DIRECTORY = resolveAppDirectory();

    private ClientConfig() {
    }

    private static String resolveSpotifyClientId() {
        String fromProperty = System.getProperty("spotify.clientId", "").trim();
        if (!fromProperty.isBlank()) {
            return fromProperty;
        }

        String fromEnvironment = System.getenv().getOrDefault("SPOTIFY_CLIENT_ID", "").trim();
        if (!fromEnvironment.isBlank()) {
            return fromEnvironment;
        }

        for (Path candidate : spotifyConfigCandidates()) {
            String value = readSpotifyClientIdFromFile(candidate);
            if (!value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static String readSpotifyClientIdFromFile(Path path) {
        if (!Files.exists(path)) {
            return "";
        }

        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
            return properties.getProperty("spotify.clientId", "").trim();
        } catch (IOException ignored) {
            return "";
        }
    }

    private static Path[] spotifyConfigCandidates() {
        Path appDir = APP_DIRECTORY;
        Path rootDir = appDir == null ? null : appDir.getParent();

        return new Path[] {
                Path.of("spotify.properties"),
                Path.of("config", "spotify.properties"),
                appDir == null ? Path.of("_missing_app_dir") : appDir.resolve("spotify.properties"),
                rootDir == null ? Path.of("_missing_root_dir") : rootDir.resolve("spotify.properties"),
                rootDir == null ? Path.of("_missing_root_config_dir") : rootDir.resolve("config").resolve("spotify.properties")
        };
    }

    private static Path resolveAppDirectory() {
        try {
            Path source = Path.of(ClientConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return Files.isDirectory(source) ? source : source.getParent();
        } catch (URISyntaxException | NullPointerException ignored) {
            return null;
        }
    }

    public static Path appMusicDirectory() {
        if (APP_DIRECTORY == null) {
            return Path.of("music");
        }
        return APP_DIRECTORY.resolve("music");
    }

    public static Path submissionBackupDirectory() {
        if (APP_DIRECTORY != null) {
            return APP_DIRECTORY.resolve("failed-submissions");
        }
        return Path.of("failed-submissions");
    }
}
