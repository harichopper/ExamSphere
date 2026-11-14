package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * StatusBadge represents small badge indicators (e.g. READY, PASSED, FAILED, WARNING).
 */
public class StatusBadge extends JPanel {
    private final String text;
    private final Color badgeColor;
    private final Color textColor;

    public StatusBadge(String text, Color badgeColor, Color textColor) {
        this.text = text;
        this.badgeColor = badgeColor;
        this.textColor = textColor;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));

        JLabel label = new JLabel(text);
        label.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        label.setForeground(textColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }

    public static StatusBadge success(String text) {
        return new StatusBadge(text, new Color(0x16, 0x65, 0x34), UIConstants.COLOR_SUCCESS);
    }

    public static StatusBadge warning(String text) {
        return new StatusBadge(text, new Color(0x78, 0x35, 0x0F), UIConstants.COLOR_WARNING);
    }

    public static StatusBadge danger(String text) {
        return new StatusBadge(text, new Color(0x7F, 0x1D, 0x1D), UIConstants.COLOR_DANGER);
    }

    public static StatusBadge info(String text) {
        return new StatusBadge(text, new Color(0x1E, 0x29, 0x3B), UIConstants.ACCENT_SECONDARY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(badgeColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

        g2.dispose();
        super.paintComponent(g);
    }
}
