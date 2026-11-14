package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.ui.components.RoundedButton;
import com.oasisinfobyte.examination.ui.components.RoundedPanel;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Shown between "Start Exam" click and actual exam start.
 * Displays rules so the student knows what to expect before the timer begins.
 */
public class ExamInstructionsPanel extends JPanel {

    private final Runnable onStartNow;
    private final Runnable onBackToDashboard;
    private JLabel titleLabel;
    private JLabel questionsLabel;
    private JLabel durationLabel;

    public ExamInstructionsPanel(Runnable onStartNow, Runnable onBackToDashboard) {
        this.onStartNow = onStartNow;
        this.onBackToDashboard = onBackToDashboard;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        buildUI();
    }

    private void buildUI() {
        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        RoundedButton backBtn = RoundedButton.createOutline("← Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(190, 38));
        backBtn.addActionListener(e -> { if (onBackToDashboard != null) onBackToDashboard.run(); });
        header.add(backBtn, BorderLayout.WEST);
        JLabel heading = new JLabel("Exam Instructions", SwingConstants.CENTER);
        heading.setFont(UIConstants.FONT_TITLE);
        heading.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(heading, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(190), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Centre card
        JPanel centreWrapper = new JPanel(new GridBagLayout());
        centreWrapper.setOpaque(false);

        RoundedPanel card = new RoundedPanel(16, UIConstants.BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));
        card.setPreferredSize(new Dimension(680, 420));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(4, 0, 4, 0);

        titleLabel = new JLabel("Java Comprehensive Assessment");
        titleLabel.setFont(UIConstants.FONT_HEADER);
        titleLabel.setForeground(UIConstants.ACCENT_PRIMARY);
        card.add(titleLabel, g);

        g.gridy++;
        g.insets = new Insets(10, 0, 16, 0);
        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        metaRow.setOpaque(false);
        questionsLabel = makeMeta("📋 30 Questions");
        durationLabel  = makeMeta("⏱  30 Minutes");
        JLabel passingLabel = makeMeta("✅ 60% to Pass");
        metaRow.add(questionsLabel);
        metaRow.add(durationLabel);
        metaRow.add(passingLabel);
        card.add(metaRow, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 8, 0);
        JLabel rulesTitle = new JLabel("Rules & Guidelines");
        rulesTitle.setFont(UIConstants.FONT_BODY_BOLD);
        rulesTitle.setForeground(UIConstants.TEXT_PRIMARY);
        card.add(rulesTitle, g);

        String[] rules = {
            "• Navigate freely between questions using Previous / Next or the Question Navigator.",
            "• Only one option may be selected per question.",
            "• Use 'Mark for Review' to flag questions you want to revisit.",
            "• Use 'Clear Answer' to remove a selected option.",
            "• The countdown timer starts immediately when you click Start.",
            "• The exam auto-submits when the timer reaches 00:00.",
            "• Manual submission shows a confirmation summary before finalising.",
            "• Answers are locked permanently once submitted — no changes possible."
        };
        for (String rule : rules) {
            g.gridy++;
            g.insets = new Insets(2, 0, 2, 0);
            JLabel lbl = new JLabel(rule);
            lbl.setFont(UIConstants.FONT_SMALL);
            lbl.setForeground(UIConstants.TEXT_SECONDARY);
            card.add(lbl, g);
        }

        g.gridy++;
        g.insets = new Insets(28, 0, 0, 0);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setOpaque(false);
        RoundedButton startBtn = RoundedButton.createPrimary("▶  Start Exam Now");
        startBtn.setPreferredSize(new Dimension(190, 44));
        startBtn.addActionListener(e -> { if (onStartNow != null) onStartNow.run(); });
        btnRow.add(startBtn);
        card.add(btnRow, g);

        centreWrapper.add(card);
        add(centreWrapper, BorderLayout.CENTER);
    }

    private JLabel makeMeta(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY_BOLD);
        l.setForeground(UIConstants.ACCENT_SECONDARY);
        return l;
    }

    public void setExamDetails(String title, int totalQuestions, int durationMinutes) {
        titleLabel.setText(title);
        questionsLabel.setText("📋 " + totalQuestions + " Questions");
        durationLabel.setText("⏱  " + durationMinutes + " Minutes");
    }
}
