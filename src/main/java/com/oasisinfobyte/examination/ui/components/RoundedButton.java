package com.oasisinfobyte.examination.ui.components;

import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern rounded button supporting primary, secondary, success, danger, and outline variants.
 */
public class RoundedButton extends JButton {
    private Color normalBackground;
    private Color hoverBackground;
    private Color pressedBackground;
    private Color textColor;
    private Color borderColor;
    private int cornerRadius;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public RoundedButton(String text, Color bg, Color hover, Color textCol, int radius) {
        super(text);
        this.normalBackground = bg;
        this.hoverBackground = hover;
        this.pressedBackground = hover.darker();
        this.textColor = textCol;
        this.borderColor = null;
        this.cornerRadius = radius;

        initButton();
    }

    public static RoundedButton createPrimary(String text) {
        return new RoundedButton(text, UIConstants.ACCENT_PRIMARY, UIConstants.ACCENT_HOVER, UIConstants.TEXT_PRIMARY, 10);
    }

    public static RoundedButton createSecondary(String text) {
        return new RoundedButton(text, UIConstants.BG_ELEVATED, UIConstants.BG_ELEVATED.brighter(), UIConstants.TEXT_PRIMARY, 10);
    }

    public static RoundedButton createSuccess(String text) {
        return new RoundedButton(text, UIConstants.COLOR_SUCCESS, UIConstants.COLOR_SUCCESS.darker(), UIConstants.TEXT_PRIMARY, 10);
    }

    public static RoundedButton createDanger(String text) {
        return new RoundedButton(text, UIConstants.COLOR_DANGER, UIConstants.COLOR_DANGER.darker(), UIConstants.TEXT_PRIMARY, 10);
    }

    public static RoundedButton createOutline(String text) {
        RoundedButton btn = new RoundedButton(text, UIConstants.BG_CARD, UIConstants.BG_ELEVATED, UIConstants.TEXT_SECONDARY, 10);
        btn.borderColor = UIConstants.BORDER_COLOR;
        return btn;
    }

    private void initButton() {
        setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(140, 42));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = false;
                    repaint();
                }
            }
        });
    }

    public void setButtonColors(Color normal, Color hover, Color textCol) {
        this.normalBackground = normal;
        this.hoverBackground = hover;
        this.pressedBackground = hover != null ? hover.darker() : normal;
        this.textColor = textCol;
        setForeground(textCol);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color bg;
        if (!isEnabled()) {
            bg = UIConstants.BG_ELEVATED.darker();
            g2.setColor(bg);
        } else if (isPressed) {
            bg = pressedBackground;
            g2.setColor(bg);
        } else if (isHovered) {
            bg = hoverBackground;
            g2.setColor(bg);
        } else {
            bg = normalBackground;
            g2.setColor(bg);
        }

        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
