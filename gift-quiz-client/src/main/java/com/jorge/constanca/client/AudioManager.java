package com.jorge.constanca.client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.MalformedURLException;
import java.net.URL;

public class AudioManager {

    private MediaPlayer backgroundPlayer;
    private AppMusicTrack currentTrack = AppMusicCatalog.defaultTrack();

    public void playBackgroundLoop(Class<?> resourceLoader) {
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.play();
                return;
            }
            playTrack(currentTrack, resourceLoader);
        } catch (Exception ignored) {
            backgroundPlayer = null;
        }
    }

    public void stopBackground() {
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
            }
        } catch (Exception ignored) {
            backgroundPlayer = null;
        }
    }

    public void pauseBackground() {
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.pause();
            }
        } catch (Exception ignored) {
            backgroundPlayer = null;
        }
    }

    public void playTrack(AppMusicTrack track, Class<?> resourceLoader) {
        if (track == null) {
            return;
        }

        currentTrack = track;
        try {
            playMedia(resolveSource(track, resourceLoader), MediaPlayer.INDEFINITE, 0.035);
        } catch (Exception ignored) {
            backgroundPlayer = null;
        }
    }

    public void resumeCurrentTrack(Class<?> resourceLoader) {
        if (backgroundPlayer != null) {
            try {
                backgroundPlayer.play();
                return;
            } catch (Exception ignored) {
                backgroundPlayer = null;
            }
        }
        playTrack(currentTrack, resourceLoader);
    }

    public AppMusicTrack currentTrack() {
        return currentTrack;
    }

    private void playMedia(String source, int cycleCount, double volume) {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
        }

        Media media = new Media(source);
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(cycleCount);
        backgroundPlayer.setVolume(volume);
        backgroundPlayer.play();
    }

    private String resolveSource(AppMusicTrack track, Class<?> resourceLoader) throws MalformedURLException {
        if (track.isBundled()) {
            URL mediaUrl = resourceLoader.getResource(track.resourcePath());
            if (mediaUrl == null) {
                throw new IllegalStateException("Missing bundled audio: " + track.resourcePath());
            }
            return mediaUrl.toExternalForm();
        }

        if (track.filePath() == null) {
            throw new IllegalStateException("Missing music file path.");
        }

        return track.filePath().toUri().toURL().toExternalForm();
    }
}
