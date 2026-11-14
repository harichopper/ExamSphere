package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.model.Question;
import com.oasisinfobyte.examination.repository.AttemptRecord;
import com.oasisinfobyte.examination.repository.AttemptRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service calculating exam scores, percentages, pass/fail status, and answer review breakdown.
 * Optionally persists attempts via AttemptRepository when one is provided.
 */
public class ResultService {
    public static final double DEFAULT_PASSING_PERCENTAGE = 60.0;
    private final SessionManager sessionManager;
    private final AttemptRepository attemptRepository;

    /** Backward-compatible constructor — no persistence. */
    public ResultService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.attemptRepository = null;
    }

    /** Full constructor with attempt persistence. */
    public ResultService(SessionManager sessionManager, AttemptRepository attemptRepository) {
        this.sessionManager = sessionManager;
        this.attemptRepository = attemptRepository;
    }

    public ExamResult calculateResult(Exam exam) {
        if (exam == null) {
            throw new IllegalArgumentException("Exam cannot be null");
        }

        List<Question> questions = exam.getQuestions();
        int totalQuestions = questions.size();
        int correctCount = 0;
        int incorrectCount = 0;
        int unansweredCount = 0;

        List<ExamResult.QuestionReviewItem> reviewItems = new ArrayList<>();

        for (int i = 0; i < totalQuestions; i++) {
            Question question = questions.get(i);
            Answer selectedAnswer = exam.getAnswer(i).orElse(null);
            Answer correctAnswer = question.getCorrectAnswer();

            boolean isAnswered = (selectedAnswer != null);
            boolean isCorrect = false;

            if (!isAnswered) {
                unansweredCount++;
            } else if (selectedAnswer == correctAnswer) {
                correctCount++;
                isCorrect = true;
            } else {
                incorrectCount++;
            }

            reviewItems.add(new ExamResult.QuestionReviewItem(
                    i + 1,
                    question.getQuestionText(),
                    question.getOptions(),
                    selectedAnswer,
                    correctAnswer,
                    isCorrect,
                    question.getExplanation()
            ));
        }

        int answeredCount = totalQuestions - unansweredCount;
        double percentage = totalQuestions > 0
                ? ((double) correctCount / totalQuestions) * 100.0 : 0.0;
        boolean passed = percentage >= DEFAULT_PASSING_PERCENTAGE;

        ExamResult result = new ExamResult(
                exam.getTitle(),
                totalQuestions,
                answeredCount,
                correctCount,
                incorrectCount,
                unansweredCount,
                correctCount,
                percentage,
                passed,
                exam.getStartTime(),
                exam.getEndTime(),
                reviewItems
        );

        sessionManager.setLatestResult(result);

        // Persist attempt to SQLite (non-fatal if repository unavailable)
        if (attemptRepository != null) {
            persistAttempt(exam, result);
        }

        return result;
    }

    private void persistAttempt(Exam exam, ExamResult result) {
        try {
            String username = sessionManager.getCurrentUser()
                    .map(u -> u.getUsername())
                    .orElse("unknown");
            AttemptRecord record = new AttemptRecord(
                    0L, username, result.examTitle(),
                    result.score(), result.totalQuestions(),
                    result.percentage(), result.passed(),
                    result.startTime(), result.endTime()
            );
            long attemptId = attemptRepository.saveAttempt(record);
            if (attemptId > 0) {
                attemptRepository.saveAnswers(attemptId, exam.getAllAnswers());
            }
        } catch (Exception e) {
            System.err.println("Warning: failed to persist attempt: " + e.getMessage());
        }
    }
}
