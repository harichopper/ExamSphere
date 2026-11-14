package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.database.DatabaseInitializer;
import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceTest {
    private DatabaseManager dbManager;
    private QuestionRepository questionRepository;
    private SessionManager sessionManager;
    private ExamService examService;
    private File tempDb;

    @BeforeEach
    void setUp() throws Exception {
        tempDb = Files.createTempFile("exam_test_", ".db").toFile();
        tempDb.deleteOnExit();
        dbManager = new DatabaseManager(tempDb.getAbsolutePath());
        new DatabaseInitializer(dbManager).initialize();
        questionRepository = new QuestionRepository(dbManager);
        sessionManager = SessionManager.getInstance();
        sessionManager.clear();
        examService = new ExamService(questionRepository, sessionManager);
    }

    @AfterEach
    void tearDown() throws Exception {
        sessionManager.clear();
        if (dbManager != null) dbManager.close();
        if (tempDb != null) tempDb.delete();
    }

    @Test
    @DisplayName("Should start new exam and bind to session manager")
    void testStartNewExam() {
        Exam exam = examService.startNewExam("Java Exam", 30);

        assertNotNull(exam);
        assertTrue(exam.getQuestions().size() >= 40,
                "Expected at least 40 questions, got: " + exam.getQuestions().size());
        assertEquals(0, exam.getCurrentQuestionIndex());
        assertFalse(exam.isSubmitted());
        assertTrue(sessionManager.getCurrentExam().isPresent());
    }

    @Test
    @DisplayName("Should record answer selection correctly")
    void testSelectAnswer() {
        examService.startNewExam("Java Exam", 30);

        examService.selectAnswer(0, Answer.B);
        assertTrue(examService.getAnswer(0).isPresent());
        assertEquals(Answer.B, examService.getAnswer(0).get());

        // Update answer
        examService.selectAnswer(0, Answer.C);
        assertEquals(Answer.C, examService.getAnswer(0).get());
    }

    @Test
    @DisplayName("Should navigate next and previous without going out of bounds")
    void testNavigation() {
        examService.startNewExam("Java Exam", 30);

        // Starts at 0
        assertFalse(examService.previousQuestion()); // cannot go before 0

        assertTrue(examService.nextQuestion()); // to 1
        assertEquals(1, sessionManager.getCurrentExam().get().getCurrentQuestionIndex());

        assertTrue(examService.previousQuestion()); // back to 0
        assertEquals(0, sessionManager.getCurrentExam().get().getCurrentQuestionIndex());

        // Jump to question index 9
        examService.jumpToQuestion(9);
        assertEquals(9, sessionManager.getCurrentExam().get().getCurrentQuestionIndex());
    }

    @Test
    @DisplayName("Should preserve selected answers across navigation")
    void testPreserveAnswersAcrossNavigation() {
        examService.startNewExam("Java Exam", 30);

        examService.selectAnswer(0, Answer.A);
        examService.nextQuestion();
        examService.selectAnswer(1, Answer.D);

        examService.previousQuestion();
        assertEquals(Answer.A, examService.getAnswer(0).get());

        examService.nextQuestion();
        assertEquals(Answer.D, examService.getAnswer(1).get());
    }

    @Test
    @DisplayName("Should submit exam and disallow answer modifications afterwards")
    void testSubmitExamLocksAnswers() {
        examService.startNewExam("Java Exam", 30);
        examService.selectAnswer(0, Answer.B);

        examService.submitExam();
        assertTrue(sessionManager.getCurrentExam().get().isSubmitted());

        assertThrows(IllegalStateException.class, () ->
                examService.selectAnswer(0, Answer.A)
        );
    }
}
