package com.oasisinfobyte.examination.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the evaluated result of an exam session.
 */
public record ExamResult(
        String examTitle,
        int totalQuestions,
        int answeredCount,
        int correctCount,
        int incorrectCount,
        int unansweredCount,
        int score,
        double percentage,
        boolean passed,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<QuestionReviewItem> reviewItems
) {
    public record QuestionReviewItem(
            int questionNumber,
            String questionText,
            Map<Answer, String> options,
            Answer userAnswer,
            Answer correctAnswer,
            boolean isCorrect,
            String explanation
    ) {}

    public List<QuestionReviewItem> reviewItems() {
        return Collections.unmodifiableList(reviewItems);
    }
}
