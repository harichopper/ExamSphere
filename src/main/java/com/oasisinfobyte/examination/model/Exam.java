package com.oasisinfobyte.examination.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Domain model representing an ongoing or completed exam session.
 */
public class Exam {
    private final String title;
    private final List<Question> questions;
    private final int durationMinutes;
    private int currentQuestionIndex = 0;
    private final Map<Integer, Answer> selectedAnswers = new HashMap<>();
    private final Set<Integer> markedForReview = new HashSet<>();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isSubmitted = false;

    public Exam(String title, List<Question> questions, int durationMinutes) {
        this.title = title;
        this.questions = Collections.unmodifiableList(questions);
        this.durationMinutes = durationMinutes;
    }

    public void start() {
        this.startTime = LocalDateTime.now();
        this.currentQuestionIndex = 0;
        this.isSubmitted = false;
        this.selectedAnswers.clear();
        this.markedForReview.clear();
    }

    public void submit() {
        this.endTime = LocalDateTime.now();
        this.isSubmitted = true;
    }

    public void setAnswer(int questionIndex, Answer answer) {
        if (isSubmitted) {
            throw new IllegalStateException("Cannot change answers after exam submission");
        }
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            selectedAnswers.put(questionIndex, answer);
        }
    }

    public Optional<Answer> getAnswer(int questionIndex) {
        return Optional.ofNullable(selectedAnswers.get(questionIndex));
    }

    public Map<Integer, Answer> getAllAnswers() {
        return Collections.unmodifiableMap(selectedAnswers);
    }

    public void markForReview(int index) {
        if (index >= 0 && index < questions.size()) {
            markedForReview.add(index);
        }
    }

    public void clearReviewMark(int index) {
        markedForReview.remove(index);
    }

    public boolean isMarkedForReview(int index) {
        return markedForReview.contains(index);
    }

    public Set<Integer> getMarkedForReview() {
        return Collections.unmodifiableSet(markedForReview);
    }

    public void clearAnswer(int index) {
        if (isSubmitted) {
            throw new IllegalStateException("Cannot change answers after exam submission");
        }
        selectedAnswers.remove(index);
    }

    public boolean nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    public boolean previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }

    public void setCurrentQuestionIndex(int index) {
        if (index >= 0 && index < questions.size()) {
            this.currentQuestionIndex = index;
        }
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }
}
