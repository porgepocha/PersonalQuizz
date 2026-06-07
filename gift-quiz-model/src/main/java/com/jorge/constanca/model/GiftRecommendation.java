package com.jorge.constanca.model;

import java.util.List;

public record GiftRecommendation(String profile, String title, String reason, List<String> suggestions) {
}
