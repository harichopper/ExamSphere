package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.ExamResult;
import com.oasisinfobyte.examination.ui.components.RoundedButton;
import com.oasisinfobyte.examination.ui.components.RoundedPanel;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

/**
 * ReviewDialog presents an in-depth breakdown of each question, showing:
 * - Question number & text
 * - All 4 options
 * - Candidate's chosen answer
 * - Correct answer
 * - Detailed explanation
 */
public class ReviewDialog extends JDialog {

    public ReviewDialog(JFrame parent, ExamResult result) {
        super(parent, "Exam Answer Review — " + result.examTitle(), true);
        setSize(850, 650);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UIConstants.BG_PRIMARY);

        initUI(result);
    }

    private void initUI(ExamResult result) {
        setLayout(new BorderLayout());

        // TOP: Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.BG_SECONDARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Question-by-Question Review");
        title.setFont(UIConstants.FONT_HEADER);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JLabel scoreSummary = new JLabel(String.format("Score: %d/%d (%.1f%%) — %s",
                result.score(), result.totalQuestions(), result.percentage(),
                result.passed() ? "PASSED" : "FAILED"));
        scoreSummary.setFont(UIConstants.FONT_BODY_BOLD);
        scoreSummary.setForeground(result.passed() ? UIConstants.COLOR_SUCCESS : UIConstants.COLOR_DANGER);
        header.add(scoreSummary, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // CENTER: Scrollable list of question cards
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.BG_PRIMARY);
        listPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        List<ExamResult.QuestionReviewItem> items = result.reviewItems();
        for (ExamResult.QuestionReviewItem item : items) {
            listPanel.add(createQuestionReviewCard(item));
            listPanel.add(Box.createVerticalStrut(16));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UIConstants.BG_PRIMARY);
        scrollPane.getViewport().setBackground(UIConstants.BG_PRIMARY);
        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM: Close Button
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 16));
        bottomBar.setBackground(UIConstants.BG_SECONDARY);

        RoundedButton closeBtn = RoundedButton.createPrimary("Close Review");
        closeBtn.setPreferredSize(new Dimension(140, 38));
        closeBtn.addActionListener(e -> dispose());
        bottomBar.add(closeBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel createQuestionReviewCard(ExamResult.QuestionReviewItem item) {
        Color cardBorder = item.isCorrect() ? UIConstants.COLOR_SUCCESS :
                (item.userAnswer() == null ? UIConstants.COLOR_WARNING : UIConstants.COLOR_DANGER);

        RoundedPanel card = new RoundedPanel(12, UIConstants.BG_CARD, cardBorder, 1);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Question Title & Status Indicator
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel qTitle = new JLabel("<html><b>Q" + item.questionNumber() + ".</b> " + item.questionText() + "</html>");
        qTitle.setFont(UIConstants.FONT_BODY_BOLD);
        qTitle.setForeground(UIConstants.TEXT_PRIMARY);
        titleRow.add(qTitle, BorderLayout.CENTER);

        String statusStr;
        Color statusColor;
        if (item.isCorrect()) {
            statusStr = "✓ CORRECT";
            statusColor = UIConstants.COLOR_SUCCESS;
        } else if (item.userAnswer() == null) {
            statusStr = "⚠ UNANSWERED";
            statusColor = UIConstants.COLOR_WARNING;
        } else {
            statusStr = "✗ INCORRECT";
            statusColor = UIConstants.COLOR_DANGER;
        }

        JLabel statusLabel = new JLabel(statusStr);
        statusLabel.setFont(UIConstants.FONT_SMALL);
        statusLabel.setForeground(statusColor);
        titleRow.add(statusLabel, BorderLayout.EAST);

        card.add(titleRow, BorderLayout.NORTH);

        // Options details
        JPanel optionsBox = new JPanel(new GridLayout(4, 1, 0, 4));
        optionsBox.setOpaque(false);

        Map<Answer, String> options = item.options();
        for (Answer ans : Answer.values()) {
            String optText = options.getOrDefault(ans, "");
            boolean isUserChoice = (ans == item.userAnswer());
            boolean isCorrectChoice = (ans == item.correctAnswer());

            JLabel optLabel = new JLabel(String.format("   %s. %s %s",
                    ans.name(), optText,
                    isCorrectChoice ? " [CORRECT ANSWER]" : (isUserChoice ? " [YOUR CHOICE]" : "")));
            optLabel.setFont(UIConstants.FONT_SMALL);

            if (isCorrectChoice) {
                optLabel.setForeground(UIConstants.COLOR_SUCCESS);
            } else if (isUserChoice) {
                optLabel.setForeground(UIConstants.COLOR_DANGER);
            } else {
                optLabel.setForeground(UIConstants.TEXT_MUTED);
            }
            optionsBox.add(optLabel);
        }
        card.add(optionsBox, BorderLayout.CENTER);

        // Explanation Footer
        if (item.explanation() != null && !item.explanation().isEmpty()) {
            JPanel expPanel = new JPanel(new BorderLayout());
            expPanel.setOpaque(false);
            expPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

            JLabel expLabel = new JLabel("<html><i style='color:#94A3B8;'>Explanation: " + item.explanation() + "</i></html>");
            expLabel.setFont(UIConstants.FONT_SMALL);
            expPanel.add(expLabel, BorderLayout.CENTER);
            card.add(expPanel, BorderLayout.SOUTH);
        }

        return card;
    }
}
