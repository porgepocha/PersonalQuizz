package com.jorge.constanca.client;

import com.jorge.constanca.model.TextAnswer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.constanca.model.QuizSubmissionRequest;
import com.jorge.constanca.model.QuizSubmissionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RemoteSubmissionClient {

    private final ObjectMapper objectMapper;

    public RemoteSubmissionClient() {
        this.objectMapper = new ObjectMapper();
    }

    public QuizSubmissionResponse sendSubmission(String participantName, List<String> selectedOptionIds, List<TextAnswer> textAnswers)
            throws IOException, InterruptedException {
        QuizSubmissionRequest payload = new QuizSubmissionRequest(
                participantName,
                selectedOptionIds,
                textAnswers,
                ClientConfig.CLIENT_VERSION
        );

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpURLConnection connection = (HttpURLConnection) new URL(ClientConfig.SUBMISSION_URL).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IOException("Backend returned unexpected status " + status);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return objectMapper.readValue(inputStream, QuizSubmissionResponse.class);
        }
    }
}
