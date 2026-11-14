package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.Exam;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.service.AuthenticationService;
import com.oasisinfobyte.examination.service.ExamService;
import com.oasisinfobyte.examination.service.ResultService;
import com.oasisinfobyte.examination.service.SessionManager;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Optional;

/**
 * Primary application frame containing a CardLayout for all post-login views.
 * Intercepts window-close events during an active exam to prevent accidental data loss.
 */
public class DashboardFrame extends JFrame {

    public static final String VIEW_DASHBOARD    = "VIEW_DASHBOARD";
    public static final String VIEW_PROFILE      = "VIEW_PROFILE";
    public static final String VIEW_INSTRUCTIONS = "VIEW_INSTRUCTIONS";
    public static final String VIEW_EXAM         = "VIEW_EXAM";
    public static final String VIEW_RESULT       = "VIEW_RESULT";

    private final AuthenticationService authService;
    private final ExamService examService;
    private final ResultService resultService;
    private final SessionManager sessionManager;
    private final Runnable onLogoutCallback;

    private final CardLayout cardLayout;
    private final JPanel containerPanel;

    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private ExamInstructionsPanel instructionsPanel;
    private ExamPanel examPanel;
    private ResultPanel resultPanel;

    public DashboardFrame(AuthenticationService authService, ExamService examService,
                          ResultService resultService, SessionManager sessionManager,
                          Runnable onLogoutCallback) {
        this.authService = authService;
        this.examService = examService;
        this.resultService = resultService;
        this.sessionManager = sessionManager;
        this.onLogoutCallback = onLogoutCallback;

        setTitle("ExamSphere — Online Examination System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BG_PRIMARY);

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);
        containerPanel.setOpaque(false);

        initViews();
        add(containerPanel);
    }

    // ── Window close guard ────────────────────────────────────────────────────

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            Optional<Exam> examOpt = sessionManager.getCurrentExam();
            if (examOpt.isPresent() && !examOpt.get().isSubmitted()) {
                showActiveExamCloseDialog();
                return; // don't propagate
            }
        }
        super.processWindowEvent(e);
    }

    private void showActiveExamCloseDialog() {
        String[] options = {"Continue Exam", "Exit & Submit", "Exit & Discard"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "<html>An exam is in progress.<br><br>"
                + "<b>Exit & Submit</b> — submits your current answers before closing.<br>"
                + "<b>Exit & Discard</b> — discards your answers and closes immediately.",
                "Exam in Progress",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {          // Exit & Submit
            try { examService.submitExam(); } catch (Exception ignored) {}
            sessionManager.clearExamSession();
            System.exit(0);
        } else if (choice == 2) {   // Exit & Discard
            sessionManager.clearExamSession();
            authService.logout();
            System.exit(0);
        }
        // choice == 0 or dialog closed → Continue Exam — do nothing
    }

    // ── View initialisation ────────────────────────────────────────────────────

    private void initViews() {
        // Dashboard
        dashboardPanel = new DashboardPanel(
                sessionManager,
                this::switchToInstructions,   // Start Exam → instructions first
                this::switchToProfile,
                this::handleLogout
        );
        containerPanel.add(dashboardPanel, VIEW_DASHBOARD);

        // Profile
        profilePanel = new ProfilePanel(authService, sessionManager, this::switchToDashboard);
        containerPanel.add(profilePanel, VIEW_PROFILE);

        // Exam Instructions
        instructionsPanel = new ExamInstructionsPanel(
                this::switchToExam,           // Start Exam Now
                this::switchToDashboard       // Back to Dashboard
        );
        containerPanel.add(instructionsPanel, VIEW_INSTRUCTIONS);

        // Exam
        examPanel = new ExamPanel(examService, resultService, sessionManager, this::handleExamFinished);
        containerPanel.add(examPanel, VIEW_EXAM);

        // Result
        resultPanel = new ResultPanel(this, this::switchToExam, this::switchToDashboard, this::handleLogout);
        containerPanel.add(resultPanel, VIEW_RESULT);

        showView(VIEW_DASHBOARD);
    }

    // ── Navigation helpers ────────────────────────────────────────────────────

    public void showView(String viewName) {
        cardLayout.show(containerPanel, viewName);
        if (VIEW_DASHBOARD.equals(viewName))      dashboardPanel.refreshDashboard();
        else if (VIEW_PROFILE.equals(viewName))   profilePanel.loadProfileData();
    }

    private void switchToDashboard()   { showView(VIEW_DASHBOARD); }
    private void switchToProfile()     { showView(VIEW_PROFILE); }
    private void switchToInstructions(){ showView(VIEW_INSTRUCTIONS); }

    private void switchToExam() {
        showView(VIEW_EXAM);
        examPanel.startExam();
    }

    private void handleExamFinished(ExamResult result) {
        resultPanel.displayResult(result);
        showView(VIEW_RESULT);
    }

    private void handleLogout() {
        // Protect against logout during active exam
        Optional<Exam> examOpt = sessionManager.getCurrentExam();
        if (examOpt.isPresent() && !examOpt.get().isSubmitted()) {
            showActiveExamCloseDialog();
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to log out?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            authService.logout();
            dispose();
            if (onLogoutCallback != null) onLogoutCallback.run();
        }
    }
}
