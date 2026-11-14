package com.oasisinfobyte.examination.service;

import com.oasisinfobyte.examination.model.User;
import com.oasisinfobyte.examination.repository.UserRepository;
import com.oasisinfobyte.examination.util.PasswordUtil;
import com.oasisinfobyte.examination.util.ValidationUtil;

import java.util.Optional;

/**
 * Service handling user authentication and profile updates.
 */
public class AuthenticationService {
    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    public AuthenticationService(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    public boolean login(String username, String password) {
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            return false;
        }

        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            sessionManager.setLoggedInUser(user);
            return true;
        }
        return false;
    }

    public boolean updateProfile(String fullName, String email) {
        Optional<User> currentUserOpt = sessionManager.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            throw new IllegalStateException("No active user session to update profile");
        }

        if (!ValidationUtil.isValidFullName(fullName)) {
            throw new IllegalArgumentException("Invalid full name format (min 2 characters)");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User currentUser = currentUserOpt.get();
        currentUser.setFullName(fullName.trim());
        currentUser.setEmail(email.trim().toLowerCase());
        userRepository.save(currentUser);
        return true;
    }

    /**
     * Changes the password for the currently logged-in user.
     *
     * @param currentPassword the user's existing password (plain text)
     * @param newPassword      the desired new password (plain text, min 8 chars)
     * @throws IllegalStateException    if no user is logged in
     * @throws IllegalArgumentException if current password is wrong or new password is too short
     */
    public void updatePassword(String currentPassword, String newPassword) {
        Optional<User> currentUserOpt = sessionManager.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            throw new IllegalStateException("No active user session");
        }
        User currentUser = currentUserOpt.get();

        if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("New password must be at least 8 characters");
        }

        currentUser.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        userRepository.save(currentUser);
    }

    public void logout() {
        sessionManager.clear();
    }
}
