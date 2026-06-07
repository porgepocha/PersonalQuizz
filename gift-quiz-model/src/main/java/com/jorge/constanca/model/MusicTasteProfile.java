package com.jorge.constanca.model;

import java.util.List;

public record MusicTasteProfile(
        String title,
        String description,
        List<String> moodTags,
        List<String> giftHints
) {
}
