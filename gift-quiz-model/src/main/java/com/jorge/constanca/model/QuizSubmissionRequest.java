package com.jorge.constanca.model;

import java.util.List;

public record QuizSubmissionRequest(
        String participantName,
        List<String> selectedOptionIds,
        List<TextAnswer> textAnswers,
        String clientVersion
) {
}
