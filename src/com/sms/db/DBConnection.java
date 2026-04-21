package com.sms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * ─────────────────────────────────────────────────────────────
 * Manages a singleton JDBC connection to MySQL.
 *
 * HOW TO USE:
 *   Connection con = DBConnection.getConnection();
 *
 * BEFORE RUNNING:
 *   Change PASSWORD below to your MySQL root password.
 * ─────────────────────────────────────────────────────────────
 */
public class DBConnection {

    // ── Change these if your MySQL setup is different ──────────
    private static final String DRIVER   = "com.mysql.jdbc.Driver";
    private static final String URL      = "jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Aaditya1145";   // ← CHANGE THIS
    // ───────────────────────────────────────────────────────────

    private static Connection connection = null;

    /**
     * Returns a singleton Connection. Reconnects if closed.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found. Add mysql-connector-java-5.x.jar to lib/");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ DB Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the connection (call on app exit if needed).
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
