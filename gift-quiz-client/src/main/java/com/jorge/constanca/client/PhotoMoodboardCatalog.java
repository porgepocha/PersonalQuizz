package com.jorge.constanca.client;

import java.util.List;

public final class PhotoMoodboardCatalog {

    private PhotoMoodboardCatalog() {
    }

    public static List<PhotoMoodboardItem> defaultGallery() {
        return List.of(
                new PhotoMoodboardItem(
                        "/photos/photo-1.jpg",
                        "Selfie"
                ),
                new PhotoMoodboardItem(
                        "/photos/photo-2.jpg",
                        "Escola"
                ),
                new PhotoMoodboardItem(
                        "/photos/photo-3.jpg",
                        "Baddie"
                ),
                new PhotoMoodboardItem(
                        "/photos/photo-4.jpg",
                        "Praia"
                ),
                new PhotoMoodboardItem(
                        "/photos/photo-5.jpg",
                        "Franja"
                )
        );
    }
}
