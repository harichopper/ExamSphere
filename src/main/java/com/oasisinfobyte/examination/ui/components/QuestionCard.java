package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.model.Question;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Premium question card — question text + 4 styled option rows.
 */
public class QuestionCard extends JPanel {

    private final JLabel questionLabel;
    private final JLabel topicLabel;
    private final JLabel qNumLabel;
    private final ButtonGroup group;
    private final Map<Answer, OptionRow> rows = new LinkedHashMap<>();
    private Consumer<Answer> onAnswerSelected;

    public QuestionCard() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(28, 28, 28, 28));

        // Question header
        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setOpaque(false);

        JPanel metaRow = new JPanel(new BorderLayout());
        metaRow.setOpaque(false);
        qNumLabel = new JLabel("Q1");
        qNumLabel.setFont(UIConstants.FONT_CAPTION);
        qNumLabel.setForeground(UIConstants.ACCENT_PRIMARY);
        topicLabel = new JLabel("");
        topicLabel.setFont(UIConstants.FONT_CAPTION);
        topicLabel.setForeground(UIConstants.TEXT_MUTED);
        metaRow.add(qNumLabel, BorderLayout.WEST);
        metaRow.add(topicLabel, BorderLayout.EAST);
        header.add(metaRow, BorderLayout.NORTH);

        questionLabel = new JLabel();
        questionLabel.setFont(UIConstants.FONT_HEADER);
        questionLabel.setForeground(UIConstants.TEXT_PRIMARY);
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        header.add(questionLabel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Options
        JPanel options = new JPanel(new GridLayout(4, 1, 0, 10));
        options.setOpaque(false);
        group = new ButtonGroup();
        for (Answer a : Answer.values()) {
            OptionRow row = new OptionRow(a);
            row.radio.addActionListener(e -> {
                rows.values().forEach(r -> r.setChosen(r.radio.isSelected()));
                if (onAnswerSelected != null) onAnswerSelected.accept(a);
            });
            group.add(row.radio);
            rows.put(a, row);
            options.add(row);
        }
        add(options, BorderLayout.CENTER);
    }

    public void setOnAnswerSelected(Consumer<Answer> l) { this.onAnswerSelected = l; }

    public void displayQuestion(int num, Question q, Answer current) {
        qNumLabel.setText("Question " + num);
        topicLabel.setText(q.getTopic());
        questionLabel.setText("<html><div style='width:560px;line-height:1.5;'>" + q.getQuestionText() + "</div></html>");
        group.clearSelection();
        Map<Answer,String> opts = q.getOptions();
        for (Answer a : Answer.values()) {
            OptionRow row = rows.get(a);
            row.setText(a.name() + ".  " + opts.getOrDefault(a, ""));
            row.radio.setSelected(a == current);
            row.setChosen(a == current);
        }
    }

    public void setOptionsEnabled(boolean enabled) {
        rows.values().forEach(r -> { r.radio.setEnabled(enabled); r.setEnabled(enabled); });
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UIConstants.BG_CARD);
        g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
        g2.setColor(UIConstants.BORDER_COLOR);
        g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
        g2.dispose(); super.paintComponent(g);
    }

    // ── Option row ────────────────────────────────────────────────────────

    private static class OptionRow extends JPanel {
        final JRadioButton radio;
        private boolean chosen = false;

        OptionRow(Answer a) {
            setOpaque(false);
            setLayout(new BorderLayout(12, 0));
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(0, 54));

            radio = new JRadioButton();
            radio.setOpaque(false);
            radio.setFocusPainted(false);
            radio.setFont(UIConstants.FONT_BODY);
            radio.setForeground(UIConstants.TEXT_PRIMARY);
            radio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(radio, BorderLayout.CENTER);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (radio.isEnabled()) radio.doClick();
                }
            });
        }

        void setText(String t) { radio.setText(t); }

        void setChosen(boolean c) { this.chosen = c; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg     = chosen ? new Color(0x63,0x66,0xF1,30) : UIConstants.BG_PRIMARY;
            Color border = chosen ? UIConstants.ACCENT_PRIMARY      : UIConstants.BORDER_COLOR;
            g2.setColor(bg);     g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
            g2.setColor(border); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
