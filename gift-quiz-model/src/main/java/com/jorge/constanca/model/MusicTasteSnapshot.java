package com.jorge.constanca.model;

import java.util.List;

public record MusicTasteSnapshot(
        List<String> topArtists,
        List<SpotifyTrack> topTracks,
        String source
) {
}
