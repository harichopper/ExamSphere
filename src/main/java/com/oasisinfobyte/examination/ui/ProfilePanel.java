package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.User;
import com.oasisinfobyte.examination.service.AuthenticationService;
import com.oasisinfobyte.examination.service.SessionManager;
import com.oasisinfobyte.examination.ui.components.ModernPasswordField;
import com.oasisinfobyte.examination.ui.components.ModernTextField;
import com.oasisinfobyte.examination.ui.components.RoundedButton;
import com.oasisinfobyte.examination.ui.components.RoundedPanel;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Profile management panel: view/edit display name and email, plus change password.
 */
public class ProfilePanel extends JPanel {

    private final AuthenticationService authService;
    private final SessionManager sessionManager;
    private final Runnable onBackToDashboard;

    // Profile fields
    private ModernTextField usernameField;
    private ModernTextField fullNameField;
    private ModernTextField emailField;
    private JLabel statusFeedbackLabel;
    private RoundedButton editButton;
    private RoundedButton saveButton;
    private RoundedButton cancelButton;
    private String originalFullName = "";
    private String originalEmail    = "";

    // Password-change fields
    private ModernPasswordField currentPassField;
    private ModernPasswordField newPassField;
    private ModernPasswordField confirmPassField;
    private JLabel passStatusLabel;

    public ProfilePanel(AuthenticationService authService, SessionManager sessionManager,
                        Runnable onBackToDashboard) {
        this.authService       = authService;
        this.sessionManager    = sessionManager;
        this.onBackToDashboard = onBackToDashboard;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        RoundedButton backBtn = RoundedButton.createOutline("← Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(180, 38));
        backBtn.addActionListener(e -> { cancelEdit(); if (onBackToDashboard != null) onBackToDashboard.run(); });
        header.add(backBtn, BorderLayout.WEST);
        JLabel title = new JLabel("User Profile", SwingConstants.CENTER);
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);
        JPanel spacer = new JPanel(); spacer.setOpaque(false); spacer.setPreferredSize(new Dimension(180,38));
        header.add(spacer, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Scroll pane wrapping two cards
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setOpaque(false);

        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.setOpaque(false);
        cards.add(buildProfileCard());
        cards.add(Box.createVerticalStrut(20));
        cards.add(buildPasswordCard());

        contentWrapper.add(cards);
        JScrollPane scroll = new JScrollPane(contentWrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildProfileCard() {
        RoundedPanel card = new RoundedPanel(16, UIConstants.BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        card.setMaximumSize(new Dimension(620, 400));
        card.setPreferredSize(new Dimension(580, 360));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        g.insets = new Insets(4, 0, 4, 0);

        JLabel header = new JLabel("👤 Account Details", SwingConstants.LEFT);
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.ACCENT_PRIMARY);
        card.add(header, g);

        g.gridy++;
        statusFeedbackLabel = new JLabel(" ");
        statusFeedbackLabel.setFont(UIConstants.FONT_SMALL);
        card.add(statusFeedbackLabel, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("Username (read-only)"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        usernameField = new ModernTextField(24); usernameField.setEnabled(false);
        card.add(usernameField, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("Full Name"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        fullNameField = new ModernTextField(24); fullNameField.setEnabled(false);
        card.add(fullNameField, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("Email Address"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        emailField = new ModernTextField(24); emailField.setEnabled(false);
        card.add(emailField, g);

        g.gridy++; g.insets = new Insets(20, 0, 0, 0);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        editButton = RoundedButton.createPrimary("EDIT PROFILE");
        editButton.addActionListener(e -> enableEdit(true));
        saveButton = RoundedButton.createSuccess("SAVE CHANGES");
        saveButton.setVisible(false);
        saveButton.addActionListener(e -> saveProfile());
        cancelButton = RoundedButton.createOutline("CANCEL");
        cancelButton.setVisible(false);
        cancelButton.addActionListener(e -> cancelEdit());
        btnRow.add(editButton); btnRow.add(saveButton); btnRow.add(cancelButton);
        card.add(btnRow, g);

        return card;
    }

    private JPanel buildPasswordCard() {
        RoundedPanel card = new RoundedPanel(16, UIConstants.BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        card.setMaximumSize(new Dimension(620, 320));
        card.setPreferredSize(new Dimension(580, 300));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        g.insets = new Insets(4, 0, 4, 0);

        JLabel header = new JLabel("🔑 Change Password", SwingConstants.LEFT);
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.ACCENT_PRIMARY);
        card.add(header, g);

        g.gridy++;
        passStatusLabel = new JLabel(" ");
        passStatusLabel.setFont(UIConstants.FONT_SMALL);
        card.add(passStatusLabel, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("Current Password"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        currentPassField = new ModernPasswordField(24);
        card.add(currentPassField, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("New Password (min 8 characters)"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        newPassField = new ModernPasswordField(24);
        card.add(newPassField, g);

        g.gridy++; g.insets = new Insets(10, 0, 2, 0);
        card.add(label("Confirm New Password"), g);
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        confirmPassField = new ModernPasswordField(24);
        card.add(confirmPassField, g);

        g.gridy++; g.insets = new Insets(20, 0, 0, 0);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        RoundedButton changeBtn = RoundedButton.createSuccess("CHANGE PASSWORD");
        changeBtn.addActionListener(e -> handlePasswordChange());
        btnRow.add(changeBtn);
        card.add(btnRow, g);

        return card;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_SMALL);
        l.setForeground(UIConstants.TEXT_SECONDARY);
        return l;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void loadProfileData() {
        sessionManager.getCurrentUser().ifPresent(user -> {
            usernameField.setText(user.getUsername());
            fullNameField.setText(user.getFullName());
            emailField.setText(user.getEmail());
            originalFullName = user.getFullName();
            originalEmail    = user.getEmail();
        });
        enableEdit(false);
        statusFeedbackLabel.setText(" ");
        clearPasswordFields();
        passStatusLabel.setText(" ");
    }

    // ── Profile edit ──────────────────────────────────────────────────────────

    private void enableEdit(boolean editable) {
        fullNameField.setEnabled(editable);
        emailField.setEnabled(editable);
        editButton.setVisible(!editable);
        saveButton.setVisible(editable);
        cancelButton.setVisible(editable);
        if (editable) fullNameField.requestFocus();
    }

    private void saveProfile() {
        String newName  = fullNameField.getText().trim();
        String newEmail = emailField.getText().trim();
        try {
            authService.updateProfile(newName, newEmail);
            originalFullName = newName; originalEmail = newEmail;
            statusFeedbackLabel.setForeground(UIConstants.COLOR_SUCCESS);
            statusFeedbackLabel.setText("✓ Profile updated successfully!");
            enableEdit(false);
        } catch (IllegalArgumentException ex) {
            statusFeedbackLabel.setForeground(UIConstants.COLOR_DANGER);
            statusFeedbackLabel.setText(ex.getMessage());
        }
    }

    private void cancelEdit() {
        fullNameField.setText(originalFullName);
        emailField.setText(originalEmail);
        statusFeedbackLabel.setText(" ");
        enableEdit(false);
    }

    // ── Password change ───────────────────────────────────────────────────────

    private void handlePasswordChange() {
        String current  = new String(currentPassField.getPassword());
        String newPass  = new String(newPassField.getPassword());
        String confirm  = new String(confirmPassField.getPassword());

        if (!newPass.equals(confirm)) {
            passStatusLabel.setForeground(UIConstants.COLOR_DANGER);
            passStatusLabel.setText("New passwords do not match");
            return;
        }
        try {
            authService.updatePassword(current, newPass);
            passStatusLabel.setForeground(UIConstants.COLOR_SUCCESS);
            passStatusLabel.setText("✓ Password changed successfully!");
            clearPasswordFields();
        } catch (IllegalArgumentException ex) {
            passStatusLabel.setForeground(UIConstants.COLOR_DANGER);
            passStatusLabel.setText(ex.getMessage());
        } catch (Exception ex) {
            passStatusLabel.setForeground(UIConstants.COLOR_DANGER);
            passStatusLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void clearPasswordFields() {
        currentPassField.setText("");
        newPassField.setText("");
        confirmPassField.setText("");
    }
}
