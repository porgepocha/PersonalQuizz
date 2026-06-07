package com.jorge.constanca.model;

public record SpotifyTrack(
        String name,
        String artist,
        String previewUrl,
        String spotifyUrl
) {
    public boolean hasPreview() {
        return previewUrl != null && !previewUrl.isBlank();
    }

    public String displayLabel() {
        if (artist == null || artist.isBlank()) {
            return name;
        }
        return name + " - " + artist;
    }

    public boolean hasSpotifyUrl() {
        return spotifyUrl != null && !spotifyUrl.isBlank();
    }

    public String embedUrl() {
        if (!hasSpotifyUrl()) {
            return "";
        }

        return spotifyUrl
                .replace("https://open.spotify.com/", "https://open.spotify.com/embed/")
                + (spotifyUrl.contains("?") ? "&" : "?")
                + "utm_source=generator";
    }
}
