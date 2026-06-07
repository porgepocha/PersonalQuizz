package com.jorge.constanca.model;

import java.util.List;

public final class MusicTasteProfiles {

    private MusicTasteProfiles() {
    }

    public static MusicTasteProfile inspiredByHerRecentSongs() {
        return new MusicTasteProfile(
                "Dreamy Indie Heart",
                "Soft melancholy, nostalgic beauty, dreamy guitars, and that quiet feeling of wanting a gift with atmosphere.",
                List.of("dreamy", "nostalgic", "indie", "soft-melancholy", "artsy"),
                List.of(
                        "Vintage-feeling keepsake",
                        "Cute but tasteful room detail",
                        "Film-night gift set",
                        "Photo book or memory box",
                        "Delicate jewelry with emotional meaning"
                )
        );
    }
}
