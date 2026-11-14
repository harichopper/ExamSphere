package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Modern dark-themed password field with focus highlighting and rounded edges.
 */
public class ModernPasswordField extends JPasswordField {
    private boolean isFocused = false;
    private int cornerRadius = 8;

    public ModernPasswordField(int columns) {
        super(columns);
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(UIConstants.BG_CARD);
        setForeground(UIConstants.TEXT_PRIMARY);
        setCaretColor(UIConstants.ACCENT_PRIMARY);
        setFont(UIConstants.FONT_BODY);
        setPreferredSize(new Dimension(getPreferredSize().width, 42));
        setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(UIConstants.BG_CARD);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Border
        g2.setColor(isFocused ? UIConstants.BORDER_FOCUS : UIConstants.BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
