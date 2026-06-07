package com.jorge.constanca.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.constanca.model.GiftRecommendation;
import com.jorge.constanca.model.QuizSubmissionRequest;
import com.jorge.constanca.model.QuizSubmissionResponse;
import com.jorge.constanca.model.StoredSubmissionView;
import com.jorge.constanca.model.TextAnswer;
import com.jorge.constanca.server.persistence.SubmissionEntity;
import com.jorge.constanca.server.persistence.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final GiftScoringService giftScoringService;
    private final ObjectMapper objectMapper;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            GiftScoringService giftScoringService,
            ObjectMapper objectMapper
    ) {
        this.submissionRepository = submissionRepository;
        this.giftScoringService = giftScoringService;
        this.objectMapper = objectMapper;
    }

    public QuizSubmissionResponse saveSubmission(QuizSubmissionRequest request) {
        GiftRecommendation recommendation = giftScoringService.score(request.selectedOptionIds());

        SubmissionEntity entity = new SubmissionEntity();
        entity.setParticipantName(blankToFallback(request.participantName(), "Mystery Girl"));
        entity.setSelectedOptionIdsJson(toJson(request.selectedOptionIds()));
        entity.setTextAnswersJson(toJsonTextAnswers(request.textAnswers()));
        entity.setProfile(recommendation.profile());
        entity.setRecommendationTitle(recommendation.title());
        entity.setRecommendationReason(recommendation.reason());
        entity.setSuggestionsJson(toJson(recommendation.suggestions()));
        entity.setClientVersion(blankToFallback(request.clientVersion(), "dev"));
        entity.setCreatedAt(OffsetDateTime.now());

        SubmissionEntity saved = submissionRepository.save(entity);
        String sweetMessage = "Recebi as tuas respostas.";

        return new QuizSubmissionResponse(
                saved.getId(),
                sweetMessage,
                recommendation.title(),
                saved.getCreatedAt().toString()
        );
    }

    public List<StoredSubmissionView> listSubmissions() {
        return submissionRepository.findAll().stream()
                .map(entity -> new StoredSubmissionView(
                        entity.getId(),
                        entity.getParticipantName(),
                        entity.getCreatedAt().toString(),
                        entity.getProfile(),
                        entity.getRecommendationTitle(),
                        entity.getRecommendationReason(),
                        fromJsonList(entity.getSelectedOptionIdsJson()),
                        fromJsonTextAnswers(entity.getTextAnswersJson()),
                        fromJsonList(entity.getSuggestionsJson())
                ))
                .toList();
    }

    private String blankToFallback(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }

    private String toJson(List<String> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize submission data.", exception);
        }
    }

    private List<String> fromJsonList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to deserialize stored submission data.", exception);
        }
    }

    private String toJsonTextAnswers(List<TextAnswer> values) {
        try {
            return objectMapper.writeValueAsString(values == null ? List.of() : values);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize text answers.", exception);
        }
    }

    private List<TextAnswer> fromJsonTextAnswers(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to deserialize text answers.", exception);
        }
    }
}
