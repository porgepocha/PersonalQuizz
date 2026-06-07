package com.jorge.constanca.model;

import java.util.List;

public record QuizQuestion(
        String id,
        String prompt,
        List<QuizOption> options,
        boolean allowsFreeText,
        String freeTextPlaceholder
) {
}
