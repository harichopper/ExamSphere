package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.service.AuthenticationService;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.function.Consumer;

/**
 * Premium login screen — gradient brand panel left, clean auth card right.
 */
public class LoginFrame extends JFrame {

    private final AuthenticationService authService;
    private final Consumer<Void> onLoginSuccess;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginFrame(AuthenticationService authService, Consumer<Void> onLoginSuccess) {
        this.authService    = authService;
        this.onLoginSuccess = onLoginSuccess;

        setTitle("ExamSphere — Online Examination System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 620);
        setMinimumSize(new Dimension(860, 520));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        getContentPane().setBackground(UIConstants.BG_PRIMARY);

        add(buildBrandPanel());
        add(buildLoginCard());
    }

    // ── Left brand panel with painted gradient ───────────────────────────

    private JPanel buildBrandPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);

                // Deep indigo → dark navy gradient
                GradientPaint gp = new GradientPaint(
                        0, 0,           new Color(0x1E, 0x1B, 0x4B),
                        0, getHeight(), new Color(0x0F, 0x11, 0x17));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                g2.setColor(Color.WHITE);
                g2.fillOval(-80, -80, 320, 320);
                g2.fillOval(getWidth() - 160, getHeight() - 200, 300, 300);

                g2.dispose();
                super.paintComponent(g);
            }

            { // init block
                setOpaque(false);
                setLayout(new GridBagLayout());
                setBorder(new EmptyBorder(60, 52, 60, 52));

                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0; c.gridy = 0;
                c.anchor = GridBagConstraints.CENTER;
                c.insets = new Insets(6, 0, 6, 0);

                // Hexagon logo (drawn)
                add(new LogoIcon(), c);

                c.gridy++;
                c.insets = new Insets(14, 0, 4, 0);
                JLabel brand = new JLabel("ExamSphere");
                brand.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 34));
                brand.setForeground(Color.WHITE);
                add(brand, c);

                c.gridy++;
                c.insets = new Insets(0, 0, 32, 0);
                JLabel sub = new JLabel("Online Examination System");
                sub.setFont(UIConstants.FONT_SUBTITLE);
                sub.setForeground(new Color(0xC7, 0xD2, 0xFE));
                add(sub, c);

                for (String[] feat : new String[][]{
                        {"⚡", "40+ Java MCQ Questions"},
                        {"⏱", "30-Minute Timed Exam"},
                        {"📊", "Instant Score & Review"},
                        {"🔒", "PBKDF2 Secure Passwords"}}) {
                    c.gridy++;
                    c.insets = new Insets(5, 0, 5, 0);
                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                    row.setOpaque(false);
                    JLabel icon = new JLabel(feat[0]);
                    icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                    JLabel text = new JLabel(feat[1]);
                    text.setFont(UIConstants.FONT_SMALL);
                    text.setForeground(new Color(0xA5, 0xB4, 0xFC));
                    row.add(icon); row.add(text);
                    add(row, c);
                }
            }
        };
    }

    // ── Right login card ─────────────────────────────────────────────────

    private JPanel buildLoginCard() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UIConstants.BG_PRIMARY);

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.setColor(UIConstants.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(44, 44, 44, 44));
        card.setPreferredSize(new Dimension(400, 490));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(0, 0, 4, 0);

        // Heading
        JLabel heading = new JLabel("Welcome back");
        heading.setFont(UIConstants.FONT_DISPLAY);
        heading.setForeground(UIConstants.TEXT_PRIMARY);
        card.add(heading, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 28, 0);
        JLabel sub = new JLabel("Sign in to your account to continue");
        sub.setFont(UIConstants.FONT_BODY);
        sub.setForeground(UIConstants.TEXT_SECONDARY);
        card.add(sub, g);

        // Error label
        g.gridy++;
        g.insets = new Insets(0, 0, 8, 0);
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.FONT_SMALL);
        errorLabel.setForeground(UIConstants.COLOR_DANGER);
        card.add(errorLabel, g);

        // Username
        g.gridy++;
        g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Username"), g);
        g.gridy++;
        g.insets = new Insets(0, 0, 16, 0);
        usernameField = styledTextField();
        usernameField.setText("student");
        card.add(usernameField, g);

        // Password
        g.gridy++;
        g.insets = new Insets(0, 0, 4, 0);
        card.add(fieldLabel("Password"), g);
        g.gridy++;
        g.insets = new Insets(0, 0, 8, 0);
        passwordField = styledPasswordField();
        passwordField.setText("student123");
        card.add(passwordField, g);

        // Show password
        g.gridy++;
        g.insets = new Insets(0, 0, 24, 0);
        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setFont(UIConstants.FONT_SMALL);
        showPass.setForeground(UIConstants.TEXT_MUTED);
        showPass.setOpaque(false);
        showPass.setFocusPainted(false);
        showPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPass.addActionListener(e -> passwordField.setEchoChar(showPass.isSelected() ? (char)0 : '•'));
        card.add(showPass, g);

        // Login button
        g.gridy++;
        g.insets = new Insets(0, 0, 20, 0);
        JButton loginBtn = primaryButton("Sign In");
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn, g);

        // Demo hint
        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JLabel hint = new JLabel("<html><div style='text-align:center;color:#64748B;'>Demo: <b style='color:#6366F1'>student</b> / <b style='color:#6366F1'>student123</b></div></html>", SwingConstants.CENTER);
        hint.setFont(UIConstants.FONT_CAPTION);
        card.add(hint, g);

        // Enter key
        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin(); }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);

        outer.add(card);
        return outer;
    }

    private void handleLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { errorLabel.setText("Username and password are required."); return; }
        if (authService.login(u, p)) {
            errorLabel.setText(" ");
            dispose();
            if (onLoginSuccess != null) onLoginSuccess.accept(null);
        } else {
            errorLabel.setText("Invalid username or password.");
            passwordField.setText("");
        }
    }

    // ── Shared helpers ───────────────────────────────────────────────────

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_SMALL);
        l.setForeground(UIConstants.TEXT_SECONDARY);
        return l;
    }

    static JTextField styledTextField() {
        JTextField f = new JTextField();
        f.setFont(UIConstants.FONT_BODY);
        f.setBackground(UIConstants.BG_INPUT);
        f.setForeground(UIConstants.TEXT_PRIMARY);
        f.setCaretColor(UIConstants.ACCENT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        f.setPreferredSize(new Dimension(0, 44));
        return f;
    }

    static JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(UIConstants.FONT_BODY);
        f.setBackground(UIConstants.BG_INPUT);
        f.setForeground(UIConstants.TEXT_PRIMARY);
        f.setCaretColor(UIConstants.ACCENT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        f.setPreferredSize(new Dimension(0, 44));
        return f;
    }

    static JButton primaryButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? new Color(0x3730A3)
                         : getModel().isRollover() ? UIConstants.ACCENT_HOVER
                         : UIConstants.ACCENT_PRIMARY;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 46));
        return b;
    }

    // ── Hexagon logo icon ─────────────────────────────────────────────────

    private static class LogoIcon extends JComponent {
        LogoIcon() { setPreferredSize(new Dimension(72, 72)); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth()/2, cy = getHeight()/2, r = 30;
            Path2D hex = new Path2D.Double();
            for (int i = 0; i < 6; i++) {
                double a = Math.PI/6 + i * Math.PI/3;
                double x = cx + r * Math.cos(a), y = cy + r * Math.sin(a);
                if (i == 0) hex.moveTo(x, y); else hex.lineTo(x, y);
            }
            hex.closePath();
            g2.setColor(new Color(0x63, 0x66, 0xF1, 40));
            g2.fill(hex);
            g2.setColor(new Color(0x63, 0x66, 0xF1));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(hex);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            String s = "⚡";
            g2.drawString(s, cx - fm.stringWidth(s)/2, cy + fm.getAscent()/2 - 2);
            g2.dispose();
        }
    }
}
