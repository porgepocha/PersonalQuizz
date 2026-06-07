package com.jorge.constanca.client;

import com.jorge.constanca.model.QuizSubmissionResponse;

import java.nio.file.Path;

public record SubmissionAttemptResult(
        boolean remoteSaved,
        QuizSubmissionResponse response,
        Path localBackupPath,
        String errorMessage
) {
    public static SubmissionAttemptResult remote(QuizSubmissionResponse response) {
        return new SubmissionAttemptResult(true, response, null, null);
    }

    public static SubmissionAttemptResult localBackup(Path localBackupPath, String errorMessage) {
        return new SubmissionAttemptResult(false, null, localBackupPath, errorMessage);
    }
}
