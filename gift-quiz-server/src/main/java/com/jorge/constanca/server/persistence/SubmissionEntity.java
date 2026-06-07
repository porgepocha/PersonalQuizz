package com.jorge.constanca.server.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "quiz_submissions")
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String participantName;

    @Column(nullable = false, length = 2000)
    private String selectedOptionIdsJson;

    @Column(nullable = false, length = 4000)
    private String textAnswersJson;

    @Column(nullable = false)
    private String profile;

    @Column(nullable = false)
    private String recommendationTitle;

    @Column(nullable = false, length = 1000)
    private String recommendationReason;

    @Column(nullable = false, length = 2000)
    private String suggestionsJson;

    @Column(nullable = false)
    private String clientVersion;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getSelectedOptionIdsJson() {
        return selectedOptionIdsJson;
    }

    public void setSelectedOptionIdsJson(String selectedOptionIdsJson) {
        this.selectedOptionIdsJson = selectedOptionIdsJson;
    }

    public String getTextAnswersJson() {
        return textAnswersJson;
    }

    public void setTextAnswersJson(String textAnswersJson) {
        this.textAnswersJson = textAnswersJson;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRecommendationTitle() {
        return recommendationTitle;
    }

    public void setRecommendationTitle(String recommendationTitle) {
        this.recommendationTitle = recommendationTitle;
    }

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public void setRecommendationReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
    }

    public String getSuggestionsJson() {
        return suggestionsJson;
    }

    public void setSuggestionsJson(String suggestionsJson) {
        this.suggestionsJson = suggestionsJson;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
