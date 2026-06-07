package com.jorge.constanca.client;

import java.nio.file.Path;

public record AppMusicTrack(
        String id,
        String title,
        String resourcePath,
        Path filePath
) {
    public static AppMusicTrack bundled(String id, String title, String resourcePath) {
        return new AppMusicTrack(id, title, resourcePath, null);
    }

    public static AppMusicTrack external(String id, String title, Path filePath) {
        return new AppMusicTrack(id, title, null, filePath);
    }

    public boolean isBundled() {
        return resourcePath != null && !resourcePath.isBlank();
    }
}
