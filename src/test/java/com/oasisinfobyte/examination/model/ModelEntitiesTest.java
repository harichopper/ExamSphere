package com.oasisinfobyte.examination.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelEntitiesTest {

    @Test
    @DisplayName("User entity getters and setters should work as expected")
    void testUserEntity() {
        User user = new User("alice", "hash123", "Alice Smith", "alice@example.com");
        assertEquals("alice", user.getUsername());
        assertEquals("hash123", user.getPasswordHash());
        assertEquals("Alice Smith", user.getFullName());
        assertEquals("alice@example.com", user.getEmail());

        user.setFullName("Alice Johnson");
        user.setEmail("alice.j@example.com");
        assertEquals("Alice Johnson", user.getFullName());
        assertEquals("alice.j@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Question entity validation and immutability checks")
    void testQuestionEntity() {
        Map<Answer, String> options = Map.of(Answer.A, "One", Answer.B, "Two", Answer.C, "Three", Answer.D, "Four");
        Question q = new Question(10, "What is 1+1?", options, Answer.B, "1+1 equals 2");

        assertEquals(10, q.getId());
        assertEquals("What is 1+1?", q.getQuestionText());
        assertEquals("Two", q.getOption(Answer.B));
        assertEquals(Answer.B, q.getCorrectAnswer());
        assertEquals("1+1 equals 2", q.getExplanation());
    }

    @Test
    @DisplayName("Exam entity lifecycle states and constraints")
    void testExamLifecycle() {
        Map<Answer, String> options = Map.of(Answer.A, "A", Answer.B, "B", Answer.C, "C", Answer.D, "D");
        List<Question> questions = List.of(
                new Question(1, "Q1", options, Answer.A, null),
                new Question(2, "Q2", options, Answer.B, null)
        );

        Exam exam = new Exam("Test Exam", questions, 10);
        assertFalse(exam.isSubmitted());
        assertNull(exam.getStartTime());

        exam.start();
        assertNotNull(exam.getStartTime());
        assertEquals(0, exam.getCurrentQuestionIndex());

        // Select answer
        exam.setAnswer(0, Answer.A);
        assertTrue(exam.getAnswer(0).isPresent());

        // Submit
        exam.submit();
        assertTrue(exam.isSubmitted());
        assertNotNull(exam.getEndTime());

        // Answering after submission must throw
        assertThrows(IllegalStateException.class, () -> exam.setAnswer(1, Answer.B));
    }
}
