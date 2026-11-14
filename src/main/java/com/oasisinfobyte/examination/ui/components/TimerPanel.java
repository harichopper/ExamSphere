package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Countdown timer panel — compact pill design with 3 colour states.
 */
public class TimerPanel extends JPanel {

    public interface TimerExpirationListener { void onTimeExpired(); }

    private int remainingSeconds;
    private final Timer swingTimer;
    private final JLabel timeLabel;
    private TimerExpirationListener expirationListener;

    public TimerPanel(int initialSeconds) {
        this.remainingSeconds = initialSeconds;
        setOpaque(false);
        setLayout(new BorderLayout(6, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setPreferredSize(new Dimension(150, 42));

        JLabel icon = new JLabel("⏱");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        icon.setForeground(UIConstants.ACCENT_SECONDARY);
        add(icon, BorderLayout.WEST);

        timeLabel = new JLabel(fmt(remainingSeconds), SwingConstants.CENTER);
        timeLabel.setFont(UIConstants.FONT_TIMER);
        timeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        add(timeLabel, BorderLayout.CENTER);

        swingTimer = new Timer(1000, e -> {
            if (remainingSeconds > 0) {
                remainingSeconds--;
                updateDisplay();
                if (remainingSeconds == 0) { stop(); if (expirationListener != null) expirationListener.onTimeExpired(); }
            }
        });
    }

    public void setExpirationListener(TimerExpirationListener l) { this.expirationListener = l; }
    public void start()  { if (!swingTimer.isRunning()) swingTimer.start(); }
    public void stop()   { swingTimer.stop(); }
    public void reset(int secs) { stop(); remainingSeconds = secs; updateDisplay(); }
    public int  getRemainingSeconds() { return remainingSeconds; }

    private void updateDisplay() {
        timeLabel.setText(fmt(remainingSeconds));
        if      (remainingSeconds <= 30) timeLabel.setForeground(UIConstants.COLOR_DANGER);
        else if (remainingSeconds <= 60) timeLabel.setForeground(UIConstants.COLOR_WARNING);
        else                             timeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        repaint();
    }

    private String fmt(int s) { return String.format("%02d:%02d", s/60, s%60); }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color bg     = remainingSeconds <= 30 ? new Color(0x3B,0x12,0x12) :
                       remainingSeconds <= 60 ? new Color(0x3B,0x2A,0x08) : UIConstants.BG_ELEVATED;
        Color border = remainingSeconds <= 30 ? UIConstants.COLOR_DANGER :
                       remainingSeconds <= 60 ? UIConstants.COLOR_WARNING : UIConstants.BORDER_COLOR;
        g2.setColor(bg);     g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
        g2.setColor(border); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
        g2.dispose(); super.paintComponent(g);
    }
}
