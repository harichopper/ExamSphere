package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.model.User;
import com.oasisinfobyte.examination.repository.AttemptRecord;
import com.oasisinfobyte.examination.repository.AttemptRepository;
import com.oasisinfobyte.examination.service.SessionManager;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

/**
 * Dashboard — welcome header, exam card, guidelines sidebar.
 */
public class DashboardPanel extends JPanel {

    private final SessionManager sessionManager;
    private final Runnable onStartExam;
    private final Runnable onOpenProfile;
    private final Runnable onLogout;

    private JLabel welcomeLabel;
    private JLabel lastScoreLabel;

    public DashboardPanel(SessionManager sessionManager, Runnable onStartExam,
                          Runnable onOpenProfile, Runnable onLogout) {
        this.sessionManager = sessionManager;
        this.onStartExam    = onStartExam;
        this.onOpenProfile  = onOpenProfile;
        this.onLogout       = onLogout;

        setOpaque(true);
        setBackground(UIConstants.BG_PRIMARY);
        setLayout(new BorderLayout(0, 24));
        setBorder(new EmptyBorder(UIConstants.INSETS_SCREEN));
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JPanel greet = new JPanel();
        greet.setLayout(new BoxLayout(greet, BoxLayout.Y_AXIS));
        greet.setOpaque(false);

        welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(UIConstants.FONT_TITLE);
        welcomeLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel sub = new JLabel("Ready to test your knowledge today?");
        sub.setFont(UIConstants.FONT_BODY);
        sub.setForeground(UIConstants.TEXT_SECONDARY);

        greet.add(welcomeLabel);
        greet.add(Box.createVerticalStrut(3));
        greet.add(sub);
        bar.add(greet, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(outlineButton("👤  Profile",  onOpenProfile));
        actions.add(dangerButton ("⏻  Logout",   onLogout));
        bar.add(actions, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildContent() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 0; g.weighty = 1.0; g.fill = GridBagConstraints.BOTH;

        g.gridx = 0; g.weightx = 0.65; g.insets = new Insets(0, 0, 0, 16);
        grid.add(buildExamCard(), g);

        g.gridx = 1; g.weightx = 0.35; g.insets = new Insets(0, 0, 0, 0);
        grid.add(buildGuidelinesCard(), g);

        return grid;
    }

    private JPanel buildExamCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, 20));

        // Header row
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel tag = new JLabel("AVAILABLE EXAM");
        tag.setFont(UIConstants.FONT_CAPTION);
        tag.setForeground(UIConstants.ACCENT_SECONDARY);
        JLabel badge = badge("READY", UIConstants.COLOR_SUCCESS);
        top.add(tag, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        JLabel title = new JLabel("Java Comprehensive Assessment");
        title.setFont(UIConstants.FONT_HEADER);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        body.add(title);

        body.add(Box.createVerticalStrut(8));
        JLabel desc = new JLabel("<html><div style='width:420px;color:#94A3B8;'>Covers OOP, Collections, Exception Handling, Multithreading, JDBC, Data Structures, and Logical Reasoning.</div></html>");
        desc.setFont(UIConstants.FONT_SMALL);
        body.add(desc);

        body.add(Box.createVerticalStrut(20));

        // Stats row
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.add(statTile("QUESTIONS",    "30",   UIConstants.ACCENT_PRIMARY));
        stats.add(statTile("TIME LIMIT",   "30 MIN", UIConstants.ACCENT_SECONDARY));
        stats.add(statTile("PASS MARK",    "60%",  UIConstants.COLOR_SUCCESS));
        lastScoreLabel = new JLabel("—");
        lastScoreLabel.setFont(UIConstants.FONT_HEADER);
        lastScoreLabel.setForeground(UIConstants.COLOR_WARNING);
        stats.add(statTileCustom("LAST SCORE", lastScoreLabel));
        body.add(stats);
        card.add(body, BorderLayout.CENTER);

        // CTA button
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        foot.setOpaque(false);
        JButton startBtn = primaryAccentButton("Start Exam  →");
        startBtn.setPreferredSize(new Dimension(190, 46));
        startBtn.addActionListener(e -> { if (onStartExam != null) onStartExam.run(); });
        foot.add(startBtn);
        card.add(foot, BorderLayout.SOUTH);

        return card;
    }

    private JPanel buildGuidelinesCard() {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, 16));

        JLabel header = new JLabel("Exam Guidelines");
        header.setFont(UIConstants.FONT_HEADER);
        header.setForeground(UIConstants.TEXT_PRIMARY);
        card.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (String[] item : new String[][]{
                {"Format",     "Multiple Choice (A, B, C, D)"},
                {"Duration",   "30 minutes countdown timer"},
                {"Navigation", "Free navigation between all questions"},
                {"Submission", "Auto at 00:00 or manual"},
                {"Marking",    "Mark questions for later review"},
                {"Safety",     "Answers locked on submit"}}) {
            list.add(guideItem(item[0], item[1]));
            list.add(Box.createVerticalStrut(10));
        }
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    // ── Refresh ───────────────────────────────────────────────────────────

    public void refreshDashboard() {
        sessionManager.getCurrentUser().ifPresent(u ->
                welcomeLabel.setText("Welcome back, " + u.getFullName() + "!"));
    }

    // ── Component helpers ─────────────────────────────────────────────────

    private JPanel card() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(UIConstants.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose(); super.paintComponent(g);
            }
            { setOpaque(false); setBorder(new EmptyBorder(28, 28, 28, 28)); }
        };
    }

    private JPanel statTile(String label, String value, Color valColor) {
        JLabel val = new JLabel(value);
        val.setFont(UIConstants.FONT_HEADER);
        val.setForeground(valColor);
        return statTileCustom(label, val);
    }

    private JPanel statTileCustom(String label, JLabel valLabel) {
        JPanel tile = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.BG_ELEVATED);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        tile.setOpaque(false);
        tile.setLayout(new BorderLayout(0,4));
        tile.setBorder(new EmptyBorder(12, 14, 12, 14));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_CAPTION);
        lbl.setForeground(UIConstants.TEXT_MUTED);
        tile.add(lbl, BorderLayout.NORTH);
        tile.add(valLabel, BorderLayout.CENTER);
        return tile;
    }

    private JPanel guideItem(String title, String desc) {
        JPanel p = new JPanel(new BorderLayout(0,2)); p.setOpaque(false);
        JLabel t = new JLabel(title); t.setFont(UIConstants.FONT_BODY_BOLD); t.setForeground(UIConstants.ACCENT_PRIMARY);
        JLabel d = new JLabel(desc);  d.setFont(UIConstants.FONT_SMALL);     d.setForeground(UIConstants.TEXT_SECONDARY);
        p.add(t, BorderLayout.NORTH); p.add(d, BorderLayout.CENTER); return p;
    }

    private JLabel badge(String text, Color fg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 20));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,99,99);
                g2.setColor(fg); g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,99,99);
                g2.dispose(); super.paintComponent(g);
            }
        };
        l.setFont(UIConstants.FONT_CAPTION);
        l.setForeground(fg);
        l.setBorder(new EmptyBorder(4,10,4,10));
        l.setOpaque(false);
        return l;
    }

    private JButton primaryAccentButton(String text) {
        return LoginFrame.primaryButton(text);
    }

    private JButton outlineButton(String text, Runnable action) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? UIConstants.BG_ELEVATED : UIConstants.BG_CARD;
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.setColor(UIConstants.BORDER_COLOR); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        styleSecBtn(b, UIConstants.TEXT_SECONDARY);
        b.addActionListener(e -> { if (action != null) action.run(); });
        return b;
    }

    private JButton dangerButton(String text, Runnable action) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? UIConstants.COLOR_DANGER : new Color(UIConstants.COLOR_DANGER.getRed(), UIConstants.COLOR_DANGER.getGreen(), UIConstants.COLOR_DANGER.getBlue(), 180);
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        styleSecBtn(b, Color.WHITE);
        b.addActionListener(e -> { if (action != null) action.run(); });
        return b;
    }

    private void styleSecBtn(JButton b, Color fg) {
        b.setFont(UIConstants.FONT_BODY_BOLD);
        b.setForeground(fg);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false); b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 38));
    }
}
