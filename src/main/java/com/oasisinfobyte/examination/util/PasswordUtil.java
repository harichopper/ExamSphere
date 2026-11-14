package com.oasisinfobyte.examination.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utility providing PBKDF2WithHmacSHA256 password hashing with per-password salts.
 */
public final class PasswordUtil {

    private static final int ITERATIONS = 310_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private PasswordUtil() {
        // Prevent instantiation
    }

    /**
     * Hashes a plain-text password using PBKDF2WithHmacSHA256 with a random salt.
     *
     * @param password plain-text password (must not be null)
     * @return a string of the form {@code hexSalt:hexDerivedKey}
     * @throws IllegalArgumentException if password is null
     */
    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] derivedKey = derive(password, salt);
        return hexEncode(salt) + ":" + hexEncode(derivedKey);
    }

    /**
     * Verifies a plain-text password against a stored PBKDF2 hash.
     *
     * @param plainPassword plain-text password to verify
     * @param storedHash    stored hash in the form {@code hexSalt:hexDerivedKey}
     * @return {@code true} if the password matches, {@code false} for any mismatch or bad input
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || storedHash.isBlank()) {
            return false;
        }
        String[] parts = storedHash.split(":", -1);
        if (parts.length != 2) {
            return false;
        }
        try {
            byte[] salt = hexDecode(parts[0]);
            byte[] expected = hexDecode(parts[1]);
            byte[] actual = derive(plainPassword, salt);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static byte[] derive(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            try {
                SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
                return skf.generateSecret(spec).getEncoded();
            } finally {
                spec.clearPassword();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(ALGORITHM + " is unavailable in this JRE.", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid key specification for " + ALGORITHM + ".", e);
        }
    }

    private static String hexEncode(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexDecode(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string: " + hex);
        }
        int len = hex.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int hi = Character.digit(hex.charAt(i), 16);
            int lo = Character.digit(hex.charAt(i + 1), 16);
            if (hi == -1 || lo == -1) {
                throw new IllegalArgumentException("Non-hex character in string: " + hex);
            }
            result[i / 2] = (byte) ((hi << 4) | lo);
        }
        return result;
    }
}
