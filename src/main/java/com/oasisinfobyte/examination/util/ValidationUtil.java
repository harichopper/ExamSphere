package com.oasisinfobyte.examination.util;

import java.util.regex.Pattern;

/**
 * Validation utility for user input verification and format validation.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[A-Za-z\\s.'-]+$"
    );

    private ValidationUtil() {
        // Prevent instantiation
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        String trimmed = username.trim();
        return trimmed.length() >= 3 && trimmed.length() <= 30 && trimmed.matches("^[A-Za-z0-9_.-]+$");
    }

    public static boolean isValidFullName(String fullName) {
        if (fullName == null) return false;
        String trimmed = fullName.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 60 && NAME_PATTERN.matcher(trimmed).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String trimmed = email.trim();
        return !trimmed.isEmpty() && trimmed.length() <= 100 && EMAIL_PATTERN.matcher(trimmed).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8 && password.length() <= 50;
    }
}
