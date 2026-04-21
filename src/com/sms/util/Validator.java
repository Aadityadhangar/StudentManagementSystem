package com.sms.util;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * Validator.java — Input Validation Utility
 * ─────────────────────────────────────────────────────────────
 * Validates all student form fields before DB operations.
 * Shows a popup warning dialog on validation failure.
 *
 * Usage:
 *   boolean ok = Validator.validateStudentForm(
 *       parentFrame, firstName, lastName, rollNo, dept, year, email, phone
 *   );
 *   if (!ok) return; // stop if invalid
 * ─────────────────────────────────────────────────────────────
 */
public class Validator {

    // Regex patterns
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[6-9]\\d{9}$");  // Indian 10-digit mobile

    // ── Basic checks ──────────────────────────────────────────

    /** True if string is null or blank */
    public static boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }

    /** True if email matches standard format */
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /** True if phone is a valid 10-digit Indian mobile number */
    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /** True if year is between 1 and 4 */
    public static boolean isValidYear(String year) {
        try {
            int y = Integer.parseInt(year);
            return y >= 1 && y <= 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ── Full form validation ──────────────────────────────────

    /**
     * Validates all fields in the Add/Edit student form.
     * Shows a popup warning if any field is invalid.
     *
     * @return true if ALL fields are valid, false if any fail
     */
    public static boolean validateStudentForm(
            JFrame parent,
            String firstName,
            String lastName,
            String rollNo,
            String dept,
            String yearStr,
            String email,
            String phone) {

        // Required fields
        if (isEmpty(firstName)) {
            showError(parent, "First Name is required."); return false;
        }
        if (isEmpty(lastName)) {
            showError(parent, "Last Name is required."); return false;
        }
        if (isEmpty(rollNo)) {
            showError(parent, "Roll Number is required."); return false;
        }
        if (isEmpty(dept)) {
            showError(parent, "Department is required."); return false;
        }
        if (!isValidYear(yearStr)) {
            showError(parent, "Year must be 1, 2, 3, or 4."); return false;
        }

        // Optional but format-checked if provided
        if (!isEmpty(email) && !isValidEmail(email)) {
            showError(parent, "Please enter a valid email address.\nExample: student@college.edu");
            return false;
        }
        if (!isEmpty(phone) && !isValidPhone(phone)) {
            showError(parent, "Phone must be a valid 10-digit Indian mobile number.\nExample: 9876543210");
            return false;
        }

        return true; // all valid
    }

    // ── Helper ────────────────────────────────────────────────
    private static void showError(JFrame parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Validation Error",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
