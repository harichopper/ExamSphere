package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResultServiceTest {
    private SessionManager sessionManager;
    private ResultService resultService;
    private List<Question> sampleQuestions;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
        sessionManager.clear();
        resultService = new ResultService(sessionManager);

        // 3 sample questions: Correct answers are A, B, C
        sampleQuestions = List.of(
                new Question(1, "Q1", Map.of(Answer.A, "OptA", Answer.B, "OptB", Answer.C, "OptC", Answer.D, "OptD"), Answer.A, "Exp1"),
                new Question(2, "Q2", Map.of(Answer.A, "OptA", Answer.B, "OptB", Answer.C, "OptC", Answer.D, "OptD"), Answer.B, "Exp2"),
                new Question(3, "Q3", Map.of(Answer.A, "OptA", Answer.B, "OptB", Answer.C, "OptC", Answer.D, "OptD"), Answer.C, "Exp3")
        );
    }

    @Test
    @DisplayName("Should accurately calculate all correct answers (100% - PASSED)")
    void testCalculateAllCorrect() {
        Exam exam = new Exam("Sample Exam", sampleQuestions, 10);
        exam.start();
        exam.setAnswer(0, Answer.A);
        exam.setAnswer(1, Answer.B);
        exam.setAnswer(2, Answer.C);
        exam.submit();

        ExamResult result = resultService.calculateResult(exam);

        assertEquals(3, result.totalQuestions());
        assertEquals(3, result.answeredCount());
        assertEquals(3, result.correctCount());
        assertEquals(0, result.incorrectCount());
        assertEquals(0, result.unansweredCount());
        assertEquals(100.0, result.percentage());
        assertTrue(result.passed());
    }

    @Test
    @DisplayName("Should accurately calculate mixed results (66.7% - PASSED >= 60%)")
    void testCalculateMixedPassed() {
        Exam exam = new Exam("Sample Exam", sampleQuestions, 10);
        exam.start();
        exam.setAnswer(0, Answer.A); // Correct
        exam.setAnswer(1, Answer.B); // Correct
        exam.setAnswer(2, Answer.D); // Incorrect (Correct is C)
        exam.submit();

        ExamResult result = resultService.calculateResult(exam);

        assertEquals(3, result.totalQuestions());
        assertEquals(3, result.answeredCount());
        assertEquals(2, result.correctCount());
        assertEquals(1, result.incorrectCount());
        assertEquals(0, result.unansweredCount());
        assertEquals(66.666, result.percentage(), 0.1);
        assertTrue(result.passed());
    }

    @Test
    @DisplayName("Should accurately calculate failed result with unanswered items (< 60%)")
    void testCalculateFailedWithUnanswered() {
        Exam exam = new Exam("Sample Exam", sampleQuestions, 10);
        exam.start();
        exam.setAnswer(0, Answer.A); // Correct
        // Q1 and Q2 left unanswered
        exam.submit();

        ExamResult result = resultService.calculateResult(exam);

        assertEquals(3, result.totalQuestions());
        assertEquals(1, result.answeredCount());
        assertEquals(1, result.correctCount());
        assertEquals(0, result.incorrectCount());
        assertEquals(2, result.unansweredCount());
        assertEquals(33.333, result.percentage(), 0.1);
        assertFalse(result.passed());
    }

    @Test
    @DisplayName("Should populate review items with questions, options, and explanations")
    void testReviewItemsPopulated() {
        Exam exam = new Exam("Sample Exam", sampleQuestions, 10);
        exam.start();
        exam.setAnswer(0, Answer.A); // Correct
        exam.setAnswer(1, Answer.A); // Incorrect
        exam.submit();

        ExamResult result = resultService.calculateResult(exam);
        List<ExamResult.QuestionReviewItem> items = result.reviewItems();

        assertEquals(3, items.size());

        // Item 0: Correct
        assertEquals(1, items.get(0).questionNumber());
        assertEquals(Answer.A, items.get(0).userAnswer());
        assertEquals(Answer.A, items.get(0).correctAnswer());
        assertTrue(items.get(0).isCorrect());
        assertEquals("Exp1", items.get(0).explanation());

        // Item 1: Incorrect
        assertEquals(2, items.get(1).questionNumber());
        assertEquals(Answer.A, items.get(1).userAnswer());
        assertEquals(Answer.B, items.get(1).correctAnswer());
        assertFalse(items.get(1).isCorrect());

        // Item 2: Unanswered
        assertEquals(3, items.get(2).questionNumber());
        assertNull(items.get(2).userAnswer());
        assertEquals(Answer.C, items.get(2).correctAnswer());
        assertFalse(items.get(2).isCorrect());
    }
}
