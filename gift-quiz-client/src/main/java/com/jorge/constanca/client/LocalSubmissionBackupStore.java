package com.jorge.constanca.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.constanca.model.QuizSubmissionRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalSubmissionBackupStore {

    private static final DateTimeFormatter FILE_STAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Path saveBackup(QuizSubmissionRequest request) throws IOException {
        Path backupDirectory = ClientConfig.submissionBackupDirectory();
        Files.createDirectories(backupDirectory);

        String fileName = "submission-" + LocalDateTime.now().format(FILE_STAMP) + ".json";
        Path target = backupDirectory.resolve(fileName);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(target.toFile(), request);
        return target;
    }
}
