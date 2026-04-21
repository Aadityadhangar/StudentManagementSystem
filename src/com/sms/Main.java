package com.sms;

import com.sms.ui.LoginFrame;
import javax.swing.*;

/**
 * Main.java — Application Entry Point
 * Run this file to launch the Student Management System
 */
public class Main {

    public static void main(String[] args) {
        // Use system look & feel for native OS styling
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch on the Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
