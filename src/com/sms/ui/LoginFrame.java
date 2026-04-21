package com.sms.ui;

import com.sms.dao.AdminDAO;
import com.sms.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame.java — Admin Login Screen
 * ─────────────────────────────────────────────────────────────
 * First screen shown when the app starts.
 * Validates credentials against the `admin` MySQL table.
 *
 * On success → opens MainFrame and closes itself.
 * On failure → shows error message, clears password field.
 *
 * Default login: admin / admin123
 * ─────────────────────────────────────────────────────────────
 */
public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JButton        btnClear;
    private AdminDAO       adminDAO;

    public LoginFrame() {
        adminDAO = new AdminDAO();
        initUI();
    }

    private void initUI() {
        setTitle("SMS — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 490);
        setLocationRelativeTo(null);   // center on screen
        setResizable(false);

        // ── Outer background ──────────────────────────────────
        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(220, 228, 242));

        // ── White card ────────────────────────────────────────
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(340, 400));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 195, 220), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 24, 0)
        ));

        // ── Blue header strip ─────────────────────────────────
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(UITheme.PRIMARY);
        header.setPreferredSize(new Dimension(340, 85));

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 4));
        headerText.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Admin Login", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(180, 210, 255));

        headerText.add(lblTitle);
        headerText.add(lblSub);
        header.add(headerText);
        card.add(header, BorderLayout.NORTH);

        // ── Form panel ────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 30, 0, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.insets  = new Insets(6, 0, 6, 0);
        gc.gridwidth = 2;

        // Avatar
        JLabel avatar = new JLabel("\uD83D\uDC64", SwingConstants.CENTER); // 👤
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        avatar.setOpaque(true);
        avatar.setBackground(UITheme.PRIMARY_LIGHT);
        avatar.setForeground(Color.WHITE);
        avatar.setPreferredSize(new Dimension(64, 64));
        avatar.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY_LIGHT, 3, true));
        gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.CENTER;
        form.add(avatar, gc);

        // Username label
        gc.gridy = 1; gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(10, 0, 2, 0);
        form.add(UITheme.createLabel("Username", UITheme.FONT_SMALL, UITheme.TEXT_MUTED), gc);

        // Username field
        gc.gridy = 2; gc.insets = new Insets(0, 0, 6, 0);
        txtUsername = UITheme.createTextField(20);
        txtUsername.setPreferredSize(new Dimension(280, 36));
        form.add(txtUsername, gc);

        // Password label
        gc.gridy = 3; gc.insets = new Insets(6, 0, 2, 0);
        form.add(UITheme.createLabel("Password", UITheme.FONT_SMALL, UITheme.TEXT_MUTED), gc);

        // Password field
        gc.gridy = 4; gc.insets = new Insets(0, 0, 6, 0);
        txtPassword = UITheme.createPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(280, 36));
        form.add(txtPassword, gc);

        // Buttons
        gc.gridy = 5; gc.insets = new Insets(16, 0, 6, 0);
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        btnLogin = UITheme.createButton("Login",  UITheme.PRIMARY_LIGHT);
        btnClear = UITheme.createButton("Clear",  UITheme.TEXT_MUTED);
        btnLogin.setPreferredSize(new Dimension(120, 38));
        btnClear.setPreferredSize(new Dimension(120, 38));
        btnRow.add(btnLogin);
        btnRow.add(btnClear);
        form.add(btnRow, gc);

        // Footer
        gc.gridy = 6; gc.insets = new Insets(10, 0, 0, 0);
        JLabel footer = UITheme.createLabel(
            "\u00A9 2024 SITRC \u2014 AIDS Department",
            UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(footer, gc);

        card.add(form, BorderLayout.CENTER);
        bg.add(card);
        setContentPane(bg);

        // ── Event Listeners ───────────────────────────────────

        // Login button click
        btnLogin.addActionListener(e -> doLogin());

        // Clear button
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
        });

        // Press Enter inside password field to login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });

        // Also Enter in username field moves focus to password
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) txtPassword.requestFocus();
            }
        });
    }

    /**
     * Reads username/password, calls AdminDAO, navigates to MainFrame on success.
     */
    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (adminDAO.validateLogin(username, password)) {
            // Success — open main app
            JOptionPane.showMessageDialog(this,
                "Welcome, " + username + "!",
                "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame().setVisible(true);
            this.dispose();  // close login window
        } else {
            // Failure
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.\nPlease try again.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
