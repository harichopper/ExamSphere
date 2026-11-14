package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.model.Answer;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Grid of numbered buttons showing per-question status:
 * CURRENT, ANSWERED, MARKED (review), ANSWERED_MARKED, or UNANSWERED.
 */
public class QuestionNavigator extends JPanel {

    public enum NavState { UNANSWERED, ANSWERED, MARKED, ANSWERED_MARKED, CURRENT }

    private final int totalQuestions;
    private final java.util.List<NavButton> buttons = new ArrayList<>();
    private Consumer<Integer> onQuestionClicked;

    public QuestionNavigator(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        setOpaque(false);
        setLayout(new GridLayout(0, 5, 8, 8));
        for (int i = 0; i < totalQuestions; i++) {
            final int idx = i;
            NavButton btn = new NavButton(String.valueOf(i + 1));
            btn.addActionListener(e -> { if (onQuestionClicked != null) onQuestionClicked.accept(idx); });
            buttons.add(btn);
            add(btn);
        }
    }

    public void setOnQuestionClicked(Consumer<Integer> listener) { this.onQuestionClicked = listener; }

    /** Original two-arg overload kept for backward compatibility. */
    public void updateState(int currentIdx, Map<Integer, Answer> selectedAnswers) {
        updateState(currentIdx, selectedAnswers, Collections.emptySet());
    }

    /** Full three-arg overload including marked-for-review set. */
    public void updateState(int currentIdx, Map<Integer, Answer> selectedAnswers, Set<Integer> marked) {
        for (int i = 0; i < totalQuestions; i++) {
            boolean isAnswered = selectedAnswers.containsKey(i) && selectedAnswers.get(i) != null;
            boolean isMarked   = marked.contains(i);
            boolean isCurrent  = (i == currentIdx);

            NavState state;
            if      (isCurrent)                state = NavState.CURRENT;
            else if (isAnswered && isMarked)   state = NavState.ANSWERED_MARKED;
            else if (isAnswered)               state = NavState.ANSWERED;
            else if (isMarked)                 state = NavState.MARKED;
            else                               state = NavState.UNANSWERED;

            buttons.get(i).setVisualState(state);
        }
    }

    // ── Inner button ─────────────────────────────────────────────────────────

    private static class NavButton extends JButton {
        private NavState state = NavState.UNANSWERED;

        NavButton(String text) {
            super(text);
            setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(42, 42));
            setForeground(UIConstants.TEXT_PRIMARY);
        }

        void setVisualState(NavState s) { this.state = s; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg, border;
            switch (state) {
                case CURRENT        -> { bg = UIConstants.ACCENT_PRIMARY;  border = UIConstants.ACCENT_SECONDARY; }
                case ANSWERED       -> { bg = UIConstants.COLOR_SUCCESS;   border = UIConstants.COLOR_SUCCESS.brighter(); }
                case MARKED         -> { bg = UIConstants.COLOR_WARNING;   border = UIConstants.COLOR_WARNING.darker(); }
                case ANSWERED_MARKED-> { bg = UIConstants.COLOR_SUCCESS;   border = UIConstants.COLOR_WARNING; }
                default             -> { bg = UIConstants.BG_ELEVATED;    border = UIConstants.BORDER_COLOR; }
            }

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
            g2.setColor(border);
            int bw = (state == NavState.ANSWERED_MARKED) ? 2 : 1;
            for (int i = 0; i < bw; i++) g2.drawRoundRect(i, i, getWidth()-1-2*i, getHeight()-1-2*i, 8, 8);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
