package com.jorge.constanca.client;

import com.jorge.constanca.model.QuizQuestion;
import com.jorge.constanca.model.TextAnswer;

import java.util.ArrayList;
import java.util.List;

public class QuizState {

    private final List<QuizQuestion> questions;
    private final List<String> selectedOptionIds = new ArrayList<>();
    private final List<TextAnswer> textAnswers = new ArrayList<>();
    private int currentQuestionIndex;

    public QuizState(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public QuizQuestion currentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public int currentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int totalQuestions() {
        return questions.size();
    }

    public void answerCurrentQuestion(String optionId, String textAnswer) {
        if (optionId != null && !optionId.isBlank()) {
            selectedOptionIds.add(optionId);
        }
        if (textAnswer != null && !textAnswer.isBlank()) {
            textAnswers.add(new TextAnswer(currentQuestion().id(), textAnswer.trim()));
        }
        currentQuestionIndex++;
    }

    public boolean isComplete() {
        return currentQuestionIndex >= questions.size();
    }

    public List<String> selectedOptionIds() {
        return List.copyOf(selectedOptionIds);
    }

    public List<TextAnswer> textAnswers() {
        return List.copyOf(textAnswers);
    }
}
