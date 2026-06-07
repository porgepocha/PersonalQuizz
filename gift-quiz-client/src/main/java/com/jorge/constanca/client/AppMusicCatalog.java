package com.jorge.constanca.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class AppMusicCatalog {

    private static final AppMusicTrack DEFAULT_TRACK = AppMusicTrack.bundled("starting-over", "Starting Over", "/audio/Starting Over.mp3");
    private static final List<AppMusicTrack> BUNDLED_TRACKS = List.of(
            DEFAULT_TRACK,
            AppMusicTrack.bundled("machine-gun", "Machine Gun", "/audio/Machine Gun_spotdown.org.mp3"),
            AppMusicTrack.bundled("holy-fucking-shit-40000", "Holy Fucking Shit, 40,000", "/audio/Holy Fucking Shit_ 40,000_spotdown.org.mp3"),
            AppMusicTrack.bundled("the-moon", "The Moon", "/audio/The Moon.mp3"),
            AppMusicTrack.bundled("beetlebum", "Beetlebum - 2012 Remaster", "/audio/Beetlebum - 2012 Remaster_spotdown.org.mp3"),
            AppMusicTrack.bundled("how-can-you-be-sure", "How Can You Be Sure?", "/audio/How Can You Be Sure__spotdown.org.mp3")
    );

    private AppMusicCatalog() {
    }

    public static AppMusicTrack defaultTrack() {
        return DEFAULT_TRACK;
    }

    public static List<AppMusicTrack> availableTracks() {
        List<AppMusicTrack> tracks = new ArrayList<>(BUNDLED_TRACKS);

        Path musicDirectory = ClientConfig.appMusicDirectory();
        if (musicDirectory != null && Files.isDirectory(musicDirectory)) {
            try (var files = Files.list(musicDirectory)) {
                files.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".mp3"))
                        .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
                        .forEach(path -> tracks.add(AppMusicTrack.external(
                                "external-" + path.getFileName().toString().toLowerCase(),
                                prettyName(path),
                                path
                        )));
            } catch (IOException ignored) {
                return tracks;
            }
        }

        return tracks;
    }

    private static String prettyName(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String base = dotIndex >= 0 ? fileName.substring(0, dotIndex) : fileName;
        return base
                .replace("_spotdown.org", "")
                .replace("__spotdown.org", "")
                .replace('-', ' ')
                .replace('_', ' ')
                .trim();
    }
}
