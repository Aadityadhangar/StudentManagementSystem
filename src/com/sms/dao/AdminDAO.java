package com.sms.dao;

import com.sms.db.DBConnection;
import java.sql.*;

/**
 * AdminDAO.java — Admin Authentication
 * ─────────────────────────────────────────────────────────────
 * Checks admin username + password against the `admin` table.
 *
 * Called by: LoginFrame.java → doLogin()
 * ─────────────────────────────────────────────────────────────
 */
public class AdminDAO {

    /**
     * Returns true if the username+password match a row in `admin` table.
     * Default credentials (from SQL script): admin / admin123
     */
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            boolean found = rs.next(); // true = credentials matched
            rs.close();
            ps.close();
            return found;
        } catch (SQLException e) {
            System.err.println("validateLogin error: " + e.getMessage());
            return false;
        }
    }
}
