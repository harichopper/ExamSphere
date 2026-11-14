package com.oasisinfobyte.examination.repository;

import com.oasisinfobyte.examination.database.DatabaseInitializer;
import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.User;
import com.oasisinfobyte.examination.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {
    private DatabaseManager dbManager;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private File tempDb;

    @BeforeEach
    void setUp() throws Exception {
        tempDb = Files.createTempFile("examsphere_test_", ".db").toFile();
        tempDb.deleteOnExit();
        dbManager = new DatabaseManager(tempDb.getAbsolutePath());
        DatabaseInitializer init = new DatabaseInitializer(dbManager);
        init.initialize();
        userRepository = new UserRepository(dbManager);
        questionRepository = new QuestionRepository(dbManager);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (dbManager != null) dbManager.close();
        if (tempDb != null) tempDb.delete();
    }

    @Test
    @DisplayName("UserRepository should be seeded with student and admin demo users")
    void testUserRepositorySeeded() {
        assertTrue(userRepository.findByUsername("student").isPresent());
        assertTrue(userRepository.findByUsername("admin").isPresent());
        assertFalse(userRepository.findByUsername("unknownUser").isPresent());
    }

    @Test
    @DisplayName("UserRepository should save and retrieve new user case-insensitively")
    void testUserSaveAndRetrieve() {
        User newUser = new User("newStudent", PasswordUtil.hashPassword("pass123456"),
                "New Student", "new@demo.com");
        userRepository.save(newUser);

        Optional<User> found = userRepository.findByUsername("NEWSTUDENT");
        assertTrue(found.isPresent());
        assertEquals("New Student", found.get().getFullName());
    }

    @Test
    @DisplayName("QuestionRepository should load at least 40 questions from seed data")
    void testQuestionRepositoryLoadsQuestions() {
        assertTrue(questionRepository.count() >= 40,
                "Expected at least 40 questions, got: " + questionRepository.count());
        assertTrue(questionRepository.findById(1).isPresent());
        assertEquals(
                "Which fundamental OOP principle is specifically concerned "
                + "with bundling data with methods and hiding internal state?",
                questionRepository.findById(1).get().getQuestionText());
    }
}
