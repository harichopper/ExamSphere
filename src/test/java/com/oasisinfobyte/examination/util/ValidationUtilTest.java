package com.oasisinfobyte.examination.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    @DisplayName("Should validate correct email addresses")
    void testValidEmails() {
        assertTrue(ValidationUtil.isValidEmail("user@example.com"));
        assertTrue(ValidationUtil.isValidEmail("student.demo@oasisinfobyte.org"));
        assertTrue(ValidationUtil.isValidEmail("name123@sub.domain.co"));
    }

    @Test
    @DisplayName("Should reject invalid email addresses")
    void testInvalidEmails() {
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("not-an-email"));
        assertFalse(ValidationUtil.isValidEmail("@missingusername.com"));
        assertFalse(ValidationUtil.isValidEmail("missingdomain@"));
    }

    @Test
    @DisplayName("Should validate valid full names")
    void testValidFullNames() {
        assertTrue(ValidationUtil.isValidFullName("John Doe"));
        assertTrue(ValidationUtil.isValidFullName("Alice Smith-Jones"));
        assertTrue(ValidationUtil.isValidFullName("O'Connor"));
    }

    @Test
    @DisplayName("Should reject invalid full names")
    void testInvalidFullNames() {
        assertFalse(ValidationUtil.isValidFullName(null));
        assertFalse(ValidationUtil.isValidFullName("   "));
        assertFalse(ValidationUtil.isValidFullName("A")); // too short
        assertFalse(ValidationUtil.isValidFullName("John123")); // digits not allowed
    }

    @Test
    @DisplayName("Should validate passwords meeting min length of 8 characters")
    void testValidPasswords() {
        assertTrue(ValidationUtil.isValidPassword("secret123"));      // 9 chars — valid
        assertTrue(ValidationUtil.isValidPassword("12345678"));       // exactly 8 — valid
        assertTrue(ValidationUtil.isValidPassword("longerpassword")); // 14 chars — valid
    }

    @Test
    @DisplayName("Should reject passwords shorter than 8 characters")
    void testInvalidPasswords() {
        assertFalse(ValidationUtil.isValidPassword(null));
        assertFalse(ValidationUtil.isValidPassword("12345"));   // 5 chars — too short
        assertFalse(ValidationUtil.isValidPassword("1234567")); // 7 chars — too short
        assertFalse(ValidationUtil.isValidPassword(""));
    }
}
