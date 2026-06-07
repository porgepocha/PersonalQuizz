package com.jorge.constanca.server.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftScoringServiceTest {

    private final GiftScoringService giftScoringService = new GiftScoringService();

    @Test
    void shouldPickLuxuryWhenMostAnswersPointToLuxury() {
        var recommendation = giftScoringService.score(List.of(
                "date-cinema",
                "surprise-perfume",
                "gift-jewelry"
        ));

        assertEquals("luxury", recommendation.profile());
        assertEquals("Elegant Muse", recommendation.title());
    }
}
