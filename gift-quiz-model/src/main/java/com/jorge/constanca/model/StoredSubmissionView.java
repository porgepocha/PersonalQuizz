package com.jorge.constanca.model;

import java.util.List;

public record StoredSubmissionView(
        Long submissionId,
        String participantName,
        String createdAt,
        List<String> selectedOptions,
        List<AnsweredText> textAnswers
) {
}
