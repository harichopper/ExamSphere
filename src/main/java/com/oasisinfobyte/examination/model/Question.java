package com.oasisinfobyte.examination.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single multiple-choice question (MCQ) within an examination.
 */
public class Question {
    private final int id;
    private final String questionText;
    private final Map<Answer, String> options;
    private final Answer correctAnswer;
    private final String explanation;
    private final String topic;

    public Question(int id, String questionText, List<String> optionList, int correctOptionIndex, String explanation, String topic) {
        if (id <= 0) {
            throw new IllegalArgumentException("Question ID must be positive: " + id);
        }
        this.id = id;
        this.questionText = Objects.requireNonNull(questionText, "Question text cannot be null").trim();
        if (optionList == null || optionList.size() < 4) {
            throw new IllegalArgumentException("Question must have at least 4 options.");
        }

        Map<Answer, String> optMap = new LinkedHashMap<>();
        Answer[] answers = Answer.values();
        for (int i = 0; i < 4 && i < optionList.size(); i++) {
            optMap.put(answers[i], optionList.get(i));
        }
        this.options = Collections.unmodifiableMap(optMap);

        this.correctAnswer = Answer.fromIndex(correctOptionIndex);
        if (this.correctAnswer == null) {
            throw new IllegalArgumentException("Invalid correct option index: " + correctOptionIndex);
        }

        this.explanation = explanation != null ? explanation.trim() : "";
        this.topic = topic != null ? topic.trim() : "General";
    }

    public Question(int id, String questionText, Map<Answer, String> options, Answer correctAnswer, String explanation) {
        this.id = id;
        this.questionText = Objects.requireNonNull(questionText, "Question text cannot be null").trim();
        this.options = Collections.unmodifiableMap(new LinkedHashMap<>(options));
        this.correctAnswer = Objects.requireNonNull(correctAnswer, "Correct answer cannot be null");
        this.explanation = explanation != null ? explanation.trim() : "";
        this.topic = "General";
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Map<Answer, String> getOptions() {
        return options;
    }

    public String getOption(Answer answer) {
        return options.get(answer);
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isCorrect(Answer candidateAnswer) {
        return this.correctAnswer == candidateAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question question)) return false;
        return id == question.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
