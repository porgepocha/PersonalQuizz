package com.jorge.constanca.server.service;

import com.jorge.constanca.model.GiftRecommendation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftScoringService {

    private static final String DEFAULT_PROFILE = "romantic";

    private final Map<String, String> optionToProfile = createOptionMap();

    public GiftRecommendation score(List<String> selectedOptionIds) {
        Map<String, Integer> scores = new HashMap<>();
        for (String selectedOptionId : selectedOptionIds) {
            String profile = optionToProfile.getOrDefault(selectedOptionId, DEFAULT_PROFILE);
            scores.merge(profile, 1, Integer::sum);
        }

        String winningProfile = scores.entrySet().stream()
                .sorted((left, right) -> Integer.compare(right.getValue(), left.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(DEFAULT_PROFILE);

        return switch (winningProfile) {
            case "cozy" -> new GiftRecommendation(
                    "cozy",
                    "Cozy Princess",
                    "She seems to value comfort, room warmth, and gentle moments that feel safe and intimate.",
                    List.of("Soft blanket set", "Cute bedside lamp", "Luxury tea or hot chocolate kit")
            );
            case "elegant", "luxury" -> new GiftRecommendation(
                    "luxury",
                    "Elegant Muse",
                    "Her choices point toward polished, timeless gifts that feel a little special and elevated.",
                    List.of("Perfume", "Delicate jewelry", "Elegant scarf or accessory")
            );
            case "cute" -> new GiftRecommendation(
                    "cute",
                    "Cute Sunshine",
                    "She leans toward playful, adorable details and gifts that feel charming and expressive.",
                    List.of("Sanrio plushie or charm", "Cute stationery set", "Mini photo printer with stickers")
            );
            case "dreamy" -> new GiftRecommendation(
                    "dreamy",
                    "Dreamy Indie Heart",
                    "Her choices suggest soft melancholy, dreamy atmosphere, and gifts that feel intimate, artful, and emotionally charged.",
                    List.of("Memory scrapbook with lyrics or notes", "Dreamy room light or projector", "Film-photo style keepsake or charm")
            );
            case "foodie" -> new GiftRecommendation(
                    "foodie",
                    "Sweet Foodie",
                    "Her answers suggest joy through treats, experiences, and little indulgences she can taste or share.",
                    List.of("Asian snack gift box", "Milk tea and dessert date", "Fancy tea room experience")
            );
            case "practical" -> new GiftRecommendation(
                    "practical",
                    "Thoughtful Minimalist",
                    "She appreciates gifts that are useful, intentional, and make everyday life nicer.",
                    List.of("Quality tumbler", "Desk accessory", "Skincare organizer")
            );
            case "cinema" -> new GiftRecommendation(
                    "cinema",
                    "Vintage Dreamer",
                    "Her choices suggest a love for artistic beauty, soft nostalgia, and gifts with a graceful old-soul feeling.",
                    List.of("Vintage-style film night set", "Beautiful art book or photo book", "Elegant keepsake inspired by classic Chinese cinema")
            );
            default -> new GiftRecommendation(
                    "romantic",
                    "Romantic Soul",
                    "Her answers suggest she values sentiment, symbolism, and gifts that carry emotional meaning.",
                    List.of("Letter plus keepsake box", "Custom necklace", "Photo book of your memories")
            );
        };
    }

    private Map<String, String> createOptionMap() {
        Map<String, String> optionMap = new HashMap<>();
        optionMap.put("date-tea", "cozy");
        optionMap.put("date-cinema", "cinema");
        optionMap.put("date-headphones", "dreamy");
        optionMap.put("surprise-letter", "romantic");
        optionMap.put("surprise-sanrio", "cute");
        optionMap.put("surprise-perfume", "luxury");
        optionMap.put("surprise-mixtape", "dreamy");
        optionMap.put("weekend-blanket", "cozy");
        optionMap.put("weekend-teahouse", "cinema");
        optionMap.put("weekend-cafe", "cute");
        optionMap.put("weekend-rain", "dreamy");
        optionMap.put("gift-keepsake", "romantic");
        optionMap.put("gift-jewelry", "luxury");
        optionMap.put("gift-film", "cinema");
        optionMap.put("gift-memory", "dreamy");
        optionMap.put("joy-plushie", "cute");
        optionMap.put("joy-milktea", "foodie");
        optionMap.put("joy-lamp", "cozy");
        optionMap.put("joy-song", "dreamy");
        optionMap.put("style-soft", "cute");
        optionMap.put("style-classic", "luxury");
        optionMap.put("style-vintage", "cinema");
        optionMap.put("style-indie", "dreamy");
        return optionMap;
    }
}
