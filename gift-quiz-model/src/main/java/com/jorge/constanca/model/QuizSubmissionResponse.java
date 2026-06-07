package com.jorge.constanca.model;

public record QuizSubmissionResponse(
        Long submissionId,
        String sweetMessage,
        String giftAura,
        String timestamp
) {
}
