package com.oasisinfobyte.examination.ui.util;

import java.awt.*;

/**
 * Design tokens for ExamSphere — dark theme, indigo accent.
 */
public final class UIConstants {
    private UIConstants() {}

    // ── Backgrounds ───────────────────────────────────────────────────────
    public static final Color BG_PRIMARY   = new Color(0x0F, 0x11, 0x17);  // #0F1117
    public static final Color BG_SECONDARY = new Color(0x16, 0x1B, 0x22);  // #161B22
    public static final Color BG_CARD      = new Color(0x1C, 0x23, 0x33);  // #1C2333
    public static final Color BG_ELEVATED  = new Color(0x21, 0x29, 0x3A);  // #21293A
    public static final Color BG_INPUT     = new Color(0x0D, 0x11, 0x17);  // darker for fields

    // ── Borders ───────────────────────────────────────────────────────────
    public static final Color BORDER_COLOR = new Color(0x2D, 0x37, 0x48);  // #2D3748
    public static final Color BORDER_FOCUS = new Color(0x63, 0x66, 0xF1);  // #6366F1

    // ── Accents ───────────────────────────────────────────────────────────
    public static final Color ACCENT_PRIMARY   = new Color(0x63, 0x66, 0xF1); // indigo
    public static final Color ACCENT_HOVER     = new Color(0x4F, 0x46, 0xE5);
    public static final Color ACCENT_SECONDARY = new Color(0x06, 0xB6, 0xD4); // cyan

    // ── Status ────────────────────────────────────────────────────────────
    public static final Color COLOR_SUCCESS = new Color(0x22, 0xC5, 0x5E); // emerald
    public static final Color COLOR_WARNING = new Color(0xF5, 0x9E, 0x0B); // amber
    public static final Color COLOR_DANGER  = new Color(0xEF, 0x44, 0x44); // red

    // ── Text ──────────────────────────────────────────────────────────────
    public static final Color TEXT_PRIMARY   = new Color(0xF1, 0xF5, 0xF9); // #F1F5F9
    public static final Color TEXT_SECONDARY = new Color(0x94, 0xA3, 0xB8); // #94A3B8
    public static final Color TEXT_MUTED     = new Color(0x64, 0x74, 0x8B); // #64748B

    // ── Navigator states ──────────────────────────────────────────────────
    public static final Color NAV_UNANSWERED = new Color(0x21, 0x29, 0x3A);
    public static final Color NAV_ANSWERED   = new Color(0x22, 0xC5, 0x5E);
    public static final Color NAV_CURRENT    = new Color(0x63, 0x66, 0xF1);
    public static final Color NAV_MARKED     = new Color(0xF5, 0x9E, 0x0B);

    // ── Typography ────────────────────────────────────────────────────────
    public static final String FONT_FAMILY = "Segoe UI";

    public static final Font FONT_DISPLAY  = new Font(FONT_FAMILY, Font.BOLD,  28);
    public static final Font FONT_TITLE    = new Font(FONT_FAMILY, Font.BOLD,  22);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.PLAIN, 15);
    public static final Font FONT_HEADER   = new Font(FONT_FAMILY, Font.BOLD,  16);
    public static final Font FONT_BODY     = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD= new Font(FONT_FAMILY, Font.BOLD,  14);
    public static final Font FONT_SMALL    = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_CAPTION  = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_TIMER    = new Font("Consolas",  Font.BOLD,  20);

    // ── Shared insets ─────────────────────────────────────────────────────
    public static final Insets INSETS_CARD   = new Insets(28, 32, 28, 32);
    public static final Insets INSETS_SCREEN = new Insets(28, 40, 28, 40);
}
