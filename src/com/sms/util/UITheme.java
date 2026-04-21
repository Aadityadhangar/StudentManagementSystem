package com.sms.util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * UITheme.java — Centralized UI Styling
 * ─────────────────────────────────────────────────────────────
 * All colors, fonts, and helper factory methods used across
 * every screen in the app. Change values here to restyle
 * the entire application at once.
 *
 * Usage:
 *   JButton b = UITheme.createButton("Save", UITheme.SUCCESS);
 *   JTextField t = UITheme.createTextField(20);
 *   UITheme.styleTable(myJTable);
 * ─────────────────────────────────────────────────────────────
 */
public class UITheme {

    // ── Color Palette ─────────────────────────────────────────
    public static final Color PRIMARY        = new Color(31,  73,  125);  // dark blue
    public static final Color PRIMARY_LIGHT  = new Color(46,  117, 182);  // medium blue
    public static final Color ACCENT         = new Color(0,   153, 153);  // teal
    public static final Color SUCCESS        = new Color(34,  139, 34);   // green
    public static final Color DANGER         = new Color(192, 57,  43);   // red
    public static final Color WARNING        = new Color(211, 132, 0);    // orange
    public static final Color BG             = new Color(240, 244, 250);  // light blue-grey background
    public static final Color SIDEBAR_BG     = new Color(26,  60,  104);  // very dark blue
    public static final Color SIDEBAR_ACTIVE = new Color(46,  117, 182);  // active menu highlight
    public static final Color WHITE          = Color.WHITE;
    public static final Color TEXT_DARK      = new Color(30,  30,  50);
    public static final Color TEXT_MUTED     = new Color(110, 110, 140);
    public static final Color TABLE_HEADER   = new Color(31,  73,  125);
    public static final Color TABLE_ROW_ALT  = new Color(235, 243, 255);  // striped row
    public static final Color BORDER_COLOR   = new Color(200, 210, 230);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,  16);
    public static final Font FONT_SUBHEAD = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BTN     = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_TABLE   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.BOLD,  13);

    // ── Factory: Styled Button ────────────────────────────────
    /**
     * Creates a rounded, colored button with hover effect.
     * @param text  label shown on button
     * @param bg    background color (use UITheme.SUCCESS, DANGER, etc.)
     */
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Darken on press, brighten on hover
                Color drawColor = getModel().isPressed()  ? bg.darker()   :
                                  getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(drawColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);  // we paint manually above
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Factory: Styled TextField ─────────────────────────────
    /**
     * Creates a text field with padding and rounded border.
     */
    public static JTextField createTextField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_DARK);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    // ── Factory: Styled PasswordField ────────────────────────
    public static JPasswordField createPasswordField(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(FONT_BODY);
        pf.setForeground(TEXT_DARK);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    // ── Factory: Styled ComboBox ──────────────────────────────
    public static JComboBox<String> createCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setBackground(WHITE);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return cb;
    }

    // ── Factory: Label ────────────────────────────────────────
    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    // ── Style JTable ──────────────────────────────────────────
    /**
     * Applies consistent styling to any JTable.
     * Call after creating your table:
     *   UITheme.styleTable(myTable);
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(198, 217, 241));
        table.setSelectionForeground(TEXT_DARK);
        table.setFillsViewportHeight(true);
        table.setBackground(WHITE);

        // Style the header row
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(TABLE_HEADER);
        header.setForeground(WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        // Alternate row colors + left padding
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? WHITE : TABLE_ROW_ALT);
                    setForeground(TEXT_DARK);
                }
                return this;
            }
        });
    }

    // ── Factory: White Card Panel ─────────────────────────────
    /**
     * A white rounded card panel with border and padding.
     * Use as a container for form sections or table cards.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        return card;
    }

    // ── Factory: Horizontal Divider ───────────────────────────
    public static JSeparator createDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
