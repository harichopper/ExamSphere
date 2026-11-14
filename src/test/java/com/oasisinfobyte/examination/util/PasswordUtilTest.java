package com.oasisinfobyte.examination.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("PBKDF2 hash should contain exactly one colon separator")
    void testPasswordHashFormat() {
        String hash = PasswordUtil.hashPassword("student123");
        assertNotNull(hash);
        // Format is hexSalt:hexDerivedKey — exactly one colon
        long colons = hash.chars().filter(c -> c == ':').count();
        assertEquals(1, colons, "Hash must have exactly one ':' separator");
        // Both parts must be non-empty hex strings
        String[] parts = hash.split(":");
        assertEquals(2, parts.length);
        assertFalse(parts[0].isEmpty(), "Salt part must not be empty");
        assertFalse(parts[1].isEmpty(), "Hash part must not be empty");
    }

    @Test
    @DisplayName("Two hashes of the same password should differ due to random salt")
    void testHashesAreUniquePerCall() {
        String hash1 = PasswordUtil.hashPassword("student123");
        String hash2 = PasswordUtil.hashPassword("student123");
        assertNotEquals(hash1, hash2, "PBKDF2 hashes must differ across calls due to random salt");
    }

    @Test
    @DisplayName("Should correctly verify matching and non-matching passwords")
    void testPasswordVerification() {
        String hash = PasswordUtil.hashPassword("correctPassword");

        assertTrue(PasswordUtil.verifyPassword("correctPassword", hash));
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hash));
        assertFalse(PasswordUtil.verifyPassword(null, hash));
        assertFalse(PasswordUtil.verifyPassword("correctPassword", null));
    }

    @Test
    @DisplayName("verifyPassword should return false for malformed stored hash")
    void testVerifyMalformedHash() {
        assertFalse(PasswordUtil.verifyPassword("anyPassword", "nocolon"));
        assertFalse(PasswordUtil.verifyPassword("anyPassword", ""));
        assertFalse(PasswordUtil.verifyPassword("anyPassword", "a:b:c"));
    }
}
