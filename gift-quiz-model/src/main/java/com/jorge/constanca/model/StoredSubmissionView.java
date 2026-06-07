package com.jorge.constanca.model;

import java.util.List;

public record StoredSubmissionView(
        Long submissionId,
        String participantName,
        String createdAt,
        String profile,
        String title,
        String reason,
        List<String> selectedOptionIds,
        List<TextAnswer> textAnswers,
        List<String> suggestions
) {
}
