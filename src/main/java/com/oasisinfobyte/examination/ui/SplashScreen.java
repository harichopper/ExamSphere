package com.oasisinfobyte.examination.ui;

import com.oasisinfobyte.examination.database.DatabaseInitializer;
import com.oasisinfobyte.examination.database.DatabaseManager;
import com.oasisinfobyte.examination.ui.util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * Branded startup window with SwingWorker DB initialization.
 */
public class SplashScreen extends JWindow {

    private final DatabaseInitializer dbInit;
    private final Runnable onReady;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public SplashScreen(DatabaseManager dbManager, Runnable onReady) {
        this.dbInit  = new DatabaseInitializer(dbManager);
        this.onReady = onReady;
        setSize(480, 280);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, new Color(0x1E,0x1B,0x4B), 0,getHeight(), new Color(0x0F,0x11,0x17));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
                // decorative circles
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f));
                g2.setColor(Color.WHITE);
                g2.fillOval(-60,-60,250,250);
                g2.fillOval(getWidth()-140,getHeight()-160,260,260);
                g2.dispose(); super.paintComponent(g);
            }
        };
        root.setOpaque(false);
        root.setBorder(BorderFactory.createLineBorder(new Color(0x2D,0x37,0x48), 1));

        // Centre content
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.setBorder(new EmptyBorder(44, 56, 20, 56));
        centre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hexagon logo
        JComponent logo = new JComponent() {
            { setPreferredSize(new Dimension(60,60)); setMaximumSize(new Dimension(60,60)); setAlignmentX(0.5f); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx=30,cy=30,r=26;
                Path2D hex = new Path2D.Double();
                for (int i=0;i<6;i++) { double a=Math.PI/6+i*Math.PI/3; double x=cx+r*Math.cos(a),y=cy+r*Math.sin(a); if(i==0) hex.moveTo(x,y); else hex.lineTo(x,y); }
                hex.closePath();
                g2.setColor(new Color(0x63,0x66,0xF1,50)); g2.fill(hex);
                g2.setColor(new Color(0x63,0x66,0xF1)); g2.setStroke(new BasicStroke(2f)); g2.draw(hex);
                g2.setFont(new Font("Segoe UI Emoji",Font.PLAIN,20)); g2.setColor(Color.WHITE);
                FontMetrics fm=g2.getFontMetrics(); String s="⚡";
                g2.drawString(s, cx-fm.stringWidth(s)/2, cy+fm.getAscent()/2-2);
                g2.dispose();
            }
        };

        JLabel name = label("ExamSphere", new Font(UIConstants.FONT_FAMILY, Font.BOLD, 28), Color.WHITE);
        name.setAlignmentX(0.5f);
        JLabel sub = label("Online Examination System", UIConstants.FONT_SUBTITLE, new Color(0xC7,0xD2,0xFE));
        sub.setAlignmentX(0.5f);
        JLabel ver = label("v2.0.0  •  OASIS INFOBYTE Java Internship", UIConstants.FONT_CAPTION, new Color(0x6B,0x72,0x80));
        ver.setAlignmentX(0.5f);

        centre.add(logo); centre.add(Box.createVerticalStrut(14));
        centre.add(name); centre.add(Box.createVerticalStrut(4));
        centre.add(sub);  centre.add(Box.createVerticalStrut(2));
        centre.add(ver);

        root.add(centre, BorderLayout.CENTER);

        // Bottom progress
        JPanel bottom = new JPanel(new BorderLayout(0,6));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 48, 32, 48));

        statusLabel = label("Initializing…", UIConstants.FONT_CAPTION, UIConstants.TEXT_MUTED);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(0x2D,0x37,0x48));
        progressBar.setForeground(UIConstants.ACCENT_PRIMARY);
        progressBar.setPreferredSize(new Dimension(0, 4));
        progressBar.setBorderPainted(false);

        bottom.add(statusLabel, BorderLayout.NORTH);
        bottom.add(progressBar, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
    }

    public void showAndInitialize() {
        setVisible(true);
        new SwingWorker<Void, Integer>() {
            @Override protected Void doInBackground() throws Exception {
                publish(15); Thread.sleep(60);
                publish(40); Thread.sleep(60);
                dbInit.initialize();
                publish(80); Thread.sleep(60);
                publish(100);
                return null;
            }
            @Override protected void process(List<Integer> chunks) {
                int v = chunks.get(chunks.size()-1);
                progressBar.setValue(v);
                statusLabel.setText(v < 40 ? "Opening database…" : v < 80 ? "Loading questions…" : "Ready!");
            }
            @Override protected void done() {
                try { get(); dispose(); if (onReady != null) onReady.run(); }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Startup failed:\n"+ex.getCause(), "ExamSphere Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        }.execute();
    }

    private static JLabel label(String t, Font f, Color c) {
        JLabel l = new JLabel(t, SwingConstants.CENTER); l.setFont(f); l.setForeground(c); return l;
    }
}
