package com.oasisinfobyte.examination;

import com.oasisinfobyte.examination.database.DatabaseInitializer;
import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.repository.AttemptRepository;
import com.oasisinfobyte.examination.repository.QuestionRepository;
import com.oasisinfobyte.examination.repository.UserRepository;
import com.oasisinfobyte.examination.service.AuthenticationService;
import com.oasisinfobyte.examination.service.ExamService;
import com.oasisinfobyte.examination.service.ResultService;
import com.oasisinfobyte.examination.service.SessionManager;
import com.oasisinfobyte.examination.ui.DashboardFrame;
import com.oasisinfobyte.examination.ui.LoginFrame;
import com.oasisinfobyte.examination.ui.SplashScreen;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Application entry point for ExamSphere — Online Examination System.
 * Bootstraps the SQLite database via SplashScreen, then wires repositories,
 * services, and UI windows on the Event Dispatch Thread.
 */
public class Main {

    // Shared application components
    private static DatabaseManager dbManager;
    private static UserRepository userRepository;
    private static QuestionRepository questionRepository;
    private static AttemptRepository attemptRepository;
    private static SessionManager sessionManager;
    private static AuthenticationService authService;
    private static ExamService examService;
    private static ResultService resultService;

    public static void main(String[] args) {
        // Install FlatLaf dark theme — professional modern L&F
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            // Fine-tune global defaults for our dark palette
            UIManager.put("Panel.background",             new java.awt.Color(0x0F1117));
            UIManager.put("Window.background",            new java.awt.Color(0x0F1117));
            UIManager.put("OptionPane.background",        new java.awt.Color(0x1C2333));
            UIManager.put("OptionPane.messageForeground", new java.awt.Color(0xF1F5F9));
            UIManager.put("Button.arc",                   12);
            UIManager.put("Component.arc",                10);
            UIManager.put("TextComponent.arc",            8);
            UIManager.put("ScrollBar.width",              8);
            UIManager.put("ScrollBar.thumbArc",           999);
            UIManager.put("ScrollBar.thumb",              new java.awt.Color(0x374151));
            UIManager.put("ScrollBar.track",              new java.awt.Color(0x1C2333));
            UIManager.put("ProgressBar.arc",              6);
            UIManager.put("TitlePane.background",         new java.awt.Color(0x161B22));
        } catch (Exception ignored) {
            // Fallback to cross-platform if FlatLaf unavailable
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception e2) { /* use default */ }
        }

        // Open the SQLite connection early so SplashScreen can use it
        try {
            dbManager = new DatabaseManager("data/examsphere.db");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Cannot open database:\n" + e.getMessage(),
                    "ExamSphere — Fatal Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Show splash screen; it initializes DB tables/seed then calls showLoginWindow
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen(dbManager, Main::initAndShowLogin);
            splash.showAndInitialize();
        });
    }

    /** Called by SplashScreen.done() on the EDT after DB init completes. */
    private static void initAndShowLogin() {
        // Wire up repositories and services
        userRepository     = new UserRepository(dbManager);
        questionRepository = new QuestionRepository(dbManager);
        attemptRepository  = new AttemptRepository(dbManager);
        sessionManager     = SessionManager.getInstance();

        authService    = new AuthenticationService(userRepository, sessionManager);
        examService    = new ExamService(questionRepository, sessionManager);
        resultService  = new ResultService(sessionManager, attemptRepository);

        showLoginWindow();
    }

    private static void showLoginWindow() {
        LoginFrame loginFrame = new LoginFrame(authService, unused -> showDashboardWindow());
        loginFrame.setVisible(true);
    }

    private static void showDashboardWindow() {
        DashboardFrame dashboardFrame = new DashboardFrame(
                authService,
                examService,
                resultService,
                sessionManager,
                Main::showLoginWindow
        );
        dashboardFrame.setVisible(true);
    }
}
