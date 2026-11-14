package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.database.DatabaseInitializer;
import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.model.User;
import com.oasisinfobyte.examination.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {
    private DatabaseManager dbManager;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private AuthenticationService authService;
    private File tempDb;

    @BeforeEach
    void setUp() throws Exception {
        tempDb = Files.createTempFile("auth_test_", ".db").toFile();
        tempDb.deleteOnExit();
        dbManager = new DatabaseManager(tempDb.getAbsolutePath());
        new DatabaseInitializer(dbManager).initialize();
        userRepository = new UserRepository(dbManager);
        sessionManager = SessionManager.getInstance();
        sessionManager.clear();
        authService = new AuthenticationService(userRepository, sessionManager);
    }

    @AfterEach
    void tearDown() throws Exception {
        sessionManager.clear();
        if (dbManager != null) dbManager.close();
        if (tempDb != null) tempDb.delete();
    }

    @Test
    @DisplayName("Should successfully authenticate valid demo student credentials")
    void testLoginSuccess() {
        boolean result = authService.login("student", "student123");
        assertTrue(result);
        assertTrue(sessionManager.isLoggedIn());
        assertEquals("student", sessionManager.getCurrentUser().get().getUsername());
    }

    @Test
    @DisplayName("Should reject invalid password")
    void testLoginFailureWrongPassword() {
        boolean result = authService.login("student", "wrongpass");
        assertFalse(result);
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    @DisplayName("Should reject non-existent user")
    void testLoginFailureUnknownUser() {
        boolean result = authService.login("doesnotexist", "password");
        assertFalse(result);
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    @DisplayName("Should reject empty or null credentials")
    void testLoginNullOrEmpty() {
        assertFalse(authService.login("", "pass"));
        assertFalse(authService.login("user", ""));
        assertFalse(authService.login(null, "pass"));
    }

    @Test
    @DisplayName("Should update profile and persist to session and repository")
    void testUpdateProfileSuccess() {
        authService.login("student", "student123");

        boolean updated = authService.updateProfile("Updated Student", "updated@examsphere.com");
        assertTrue(updated);

        User currentUser = sessionManager.getCurrentUser().get();
        assertEquals("Updated Student", currentUser.getFullName());
        assertEquals("updated@examsphere.com", currentUser.getEmail());

        User repoUser = userRepository.findByUsername("student").get();
        assertEquals("Updated Student", repoUser.getFullName());
    }

    @Test
    @DisplayName("Should reject invalid email during profile update")
    void testUpdateProfileInvalidEmail() {
        authService.login("student", "student123");
        assertThrows(IllegalArgumentException.class, () ->
                authService.updateProfile("Updated Student", "not-a-valid-email")
        );
    }

    @Test
    @DisplayName("Should reject invalid name during profile update")
    void testUpdateProfileInvalidName() {
        authService.login("student", "student123");
        assertThrows(IllegalArgumentException.class, () ->
                authService.updateProfile("X", "valid@email.com")
        );
    }

    @Test
    @DisplayName("Should clear session upon logout")
    void testLogoutClearsSession() {
        authService.login("student", "student123");
        assertTrue(sessionManager.isLoggedIn());

        authService.logout();
        assertFalse(sessionManager.isLoggedIn());
        assertTrue(sessionManager.getCurrentUser().isEmpty());
    }
}
