package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.ui.components.RoundedButton;
import com.oasisinfobyte.examination.ui.components.RoundedPanel;
import com.oasisinfobyte.examination.ui.components.StatusBadge;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;

/**
 * ResultPanel presents the summary of the examination:
 * - Pass/Fail status
 * - Large score percentage badge
 * - Stat grid: Total, Correct, Incorrect, Unanswered
 * - Action buttons: VIEW ANSWERS, RETAKE EXAM, BACK TO DASHBOARD, LOGOUT
 */
public class ResultPanel extends JPanel {
    private final JFrame parentFrame;
    private final Runnable onRetakeExam;
    private final Runnable onBackToDashboard;
    private final Runnable onLogout;

    private ExamResult currentResult;

    private JLabel statusBannerLabel;
    private JLabel scorePercentLabel;
    private JLabel examTitleLabel;
    private JLabel totalQuestionsVal;
    private JLabel correctVal;
    private JLabel incorrectVal;
    private JLabel unansweredVal;
    private JLabel timeSubmittedVal;
    private JLabel timeTakenVal;

    public ResultPanel(JFrame parentFrame, Runnable onRetakeExam, Runnable onBackToDashboard, Runnable onLogout) {
        this.parentFrame = parentFrame;
        this.onRetakeExam = onRetakeExam;
        this.onBackToDashboard = onBackToDashboard;
        this.onLogout = onLogout;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        initUI();
    }

    private void initUI() {
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        RoundedPanel card = new RoundedPanel(16, UIConstants.BG_CARD);
        card.setLayout(new BorderLayout(0, 24));
        card.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));
        card.setPreferredSize(new Dimension(680, 520));

        // TOP: Status Header & Large Score
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);

        statusBannerLabel = new JLabel("EXAM COMPLETED", SwingConstants.CENTER);
        statusBannerLabel.setFont(UIConstants.FONT_SMALL);
        statusBannerLabel.setForeground(UIConstants.TEXT_SECONDARY);
        statusBannerLabel.setAlignmentX(CENTER_ALIGNMENT);

        scorePercentLabel = new JLabel("0.0%", SwingConstants.CENTER);
        scorePercentLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 54));
        scorePercentLabel.setForeground(UIConstants.COLOR_SUCCESS);
        scorePercentLabel.setAlignmentX(CENTER_ALIGNMENT);

        examTitleLabel = new JLabel("Java Fundamentals Assessment", SwingConstants.CENTER);
        examTitleLabel.setFont(UIConstants.FONT_HEADER);
        examTitleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        examTitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        topSection.add(statusBannerLabel);
        topSection.add(Box.createVerticalStrut(8));
        topSection.add(scorePercentLabel);
        topSection.add(Box.createVerticalStrut(4));
        topSection.add(examTitleLabel);

        card.add(topSection, BorderLayout.NORTH);

        // CENTER: 5-tile Metrics Grid (Total, Correct, Incorrect, Unanswered, Time Taken)
        JPanel metricsGrid = new JPanel(new GridLayout(1, 5, 12, 0));
        metricsGrid.setOpaque(false);

        totalQuestionsVal = new JLabel("30", SwingConstants.CENTER);
        correctVal = new JLabel("0", SwingConstants.CENTER);
        incorrectVal = new JLabel("0", SwingConstants.CENTER);
        unansweredVal = new JLabel("0", SwingConstants.CENTER);
        timeTakenVal = new JLabel("—", SwingConstants.CENTER);

        metricsGrid.add(createMetricCard("TOTAL",      totalQuestionsVal, UIConstants.TEXT_PRIMARY));
        metricsGrid.add(createMetricCard("CORRECT",    correctVal,        UIConstants.COLOR_SUCCESS));
        metricsGrid.add(createMetricCard("INCORRECT",  incorrectVal,      UIConstants.COLOR_DANGER));
        metricsGrid.add(createMetricCard("UNANSWERED", unansweredVal,     UIConstants.COLOR_WARNING));
        metricsGrid.add(createMetricCard("TIME TAKEN", timeTakenVal,      UIConstants.ACCENT_SECONDARY));

        card.add(metricsGrid, BorderLayout.CENTER);

        // BOTTOM: Action buttons row
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setOpaque(false);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonRow.setOpaque(false);

        RoundedButton viewAnswersBtn = RoundedButton.createPrimary("VIEW ANSWERS");
        viewAnswersBtn.setPreferredSize(new Dimension(150, 42));
        viewAnswersBtn.addActionListener(e -> {
            if (currentResult != null) {
                ReviewDialog dialog = new ReviewDialog(parentFrame, currentResult);
                dialog.setVisible(true);
            }
        });
        buttonRow.add(viewAnswersBtn);

        RoundedButton retakeBtn = RoundedButton.createSecondary("RETAKE EXAM");
        retakeBtn.setPreferredSize(new Dimension(140, 42));
        retakeBtn.addActionListener(e -> {
            if (onRetakeExam != null) onRetakeExam.run();
        });
        buttonRow.add(retakeBtn);

        RoundedButton dashboardBtn = RoundedButton.createOutline("DASHBOARD");
        dashboardBtn.setPreferredSize(new Dimension(140, 42));
        dashboardBtn.addActionListener(e -> {
            if (onBackToDashboard != null) onBackToDashboard.run();
        });
        buttonRow.add(dashboardBtn);

        RoundedButton logoutBtn = RoundedButton.createDanger("LOGOUT");
        logoutBtn.setPreferredSize(new Dimension(120, 42));
        logoutBtn.addActionListener(e -> {
            if (onLogout != null) onLogout.run();
        });
        buttonRow.add(logoutBtn);

        bottomSection.add(buttonRow);
        card.add(bottomSection, BorderLayout.SOUTH);

        centerContainer.add(card);
        add(centerContainer, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color color) {
        RoundedPanel p = new RoundedPanel(10, UIConstants.BG_ELEVATED);
        p.setLayout(new BorderLayout(0, 4));
        p.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(UIConstants.FONT_SMALL);
        titleLbl.setForeground(UIConstants.TEXT_MUTED);
        p.add(titleLbl, BorderLayout.NORTH);

        valueLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 22));
        valueLabel.setForeground(color);
        p.add(valueLabel, BorderLayout.CENTER);

        return p;
    }

    public void displayResult(ExamResult result) {
        this.currentResult = result;

        examTitleLabel.setText(result.examTitle());
        scorePercentLabel.setText(String.format("%.0f%%", result.percentage()));

        if (result.passed()) {
            statusBannerLabel.setText("EXAM COMPLETED — STATUS: PASSED");
            scorePercentLabel.setForeground(UIConstants.COLOR_SUCCESS);
        } else {
            statusBannerLabel.setText("EXAM COMPLETED — STATUS: FAILED");
            scorePercentLabel.setForeground(UIConstants.COLOR_DANGER);
        }

        totalQuestionsVal.setText(String.valueOf(result.totalQuestions()));
        correctVal.setText(String.valueOf(result.correctCount()));
        incorrectVal.setText(String.valueOf(result.incorrectCount()));
        unansweredVal.setText(String.valueOf(result.unansweredCount()));

        // Time taken
        if (result.startTime() != null && result.endTime() != null) {
            java.time.Duration dur = java.time.Duration.between(result.startTime(), result.endTime());
            long secs = Math.abs(dur.getSeconds());
            if (secs >= 3600) {
                timeTakenVal.setText(String.format("%d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60));
            } else {
                timeTakenVal.setText(String.format("%02d:%02d", secs / 60, secs % 60));
            }
        } else {
            timeTakenVal.setText("N/A");
        }
    }
}
