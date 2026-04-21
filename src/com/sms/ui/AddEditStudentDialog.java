package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import com.sms.util.UITheme;
import com.sms.util.Validator;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * AddEditStudentDialog.java — Add / Edit Student Modal Dialog
 * ─────────────────────────────────────────────────────────────
 * A single dialog used for BOTH adding and editing students.
 *
 * To ADD a new student:
 *   new AddEditStudentDialog(parent, dao, null, callback);
 *
 * To EDIT an existing student:
 *   new AddEditStudentDialog(parent, dao, studentObject, callback);
 *
 * The callback (Runnable) is called after a successful save/update
 * so the parent can refresh its table.
 * ─────────────────────────────────────────────────────────────
 */
public class AddEditStudentDialog extends JDialog {

    // Dependencies
    private final StudentDAO dao;
    private final Student    existing;    // null = Add mode, non-null = Edit mode
    private final Runnable   onSuccess;   // refresh callback

    // Form fields
    private JTextField  txtFirstName;
    private JTextField  txtLastName;
    private JTextField  txtRollNo;
    private JTextField  txtEmail;
    private JTextField  txtPhone;
    private JTextField  txtDob;
    private JTextField  txtAddress;
    private JComboBox<String> cbDept;
    private JComboBox<String> cbYear;

    // Dropdown options
    private static final String[] DEPARTMENTS = {
        "AI & Data Science",
        "Computer Engineering",
        "Mechanical Engineering",
        "Electronics & Telecom",
        "Civil Engineering",
        "Information Technology"
    };
    private static final String[] YEARS = { "1", "2", "3", "4" };

    // ── Constructor ───────────────────────────────────────────
    public AddEditStudentDialog(JFrame parent, StudentDAO dao,
                                 Student existing, Runnable onSuccess) {
        super(parent,
              existing == null ? "Add New Student" : "Edit Student",
              true); // modal = true
        this.dao       = dao;
        this.existing  = existing;
        this.onSuccess = onSuccess;

        buildUI();

        if (existing != null) {
            fillFields(existing); // pre-fill for Edit mode
        }

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    // ─────────────────────────────────────────────────────────
    //  UI CONSTRUCTION
    // ─────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);

        // ── Header ────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        String icon  = existing == null ? "\u2795" : "\u270F";
        String title = existing == null ? "  Add New Student" : "   Edit Student";
        JLabel lblH = UITheme.createLabel(icon + title, UITheme.FONT_HEADING, Color.WHITE);
        header.add(lblH, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── Form card ─────────────────────────────────────────
        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int FW = 230; // field width

        // Row 0 labels: First Name / Last Name
        addLabelRow(card, gc, 0, "First Name *", "Last Name *");
        // Row 1 fields
        txtFirstName = makeField(FW);
        txtLastName  = makeField(FW);
        addFieldRow(card, gc, 1, txtFirstName, txtLastName);

        // Row 2 labels: Roll Number / Department
        addLabelRow(card, gc, 2, "Roll Number *", "Department *");
        // Row 3 fields
        txtRollNo = makeField(FW);
        cbDept    = UITheme.createCombo(DEPARTMENTS);
        cbDept.setPreferredSize(new Dimension(FW, 36));
        addFieldRow(card, gc, 3, txtRollNo, cbDept);

        // Row 4 labels: Year / Date of Birth
        addLabelRow(card, gc, 4, "Year *  (1–4)", "Date of Birth  (DD-MM-YYYY)");
        // Row 5 fields
        cbYear = UITheme.createCombo(YEARS);
        cbYear.setPreferredSize(new Dimension(FW, 36));
        txtDob = makeField(FW);
        addFieldRow(card, gc, 5, cbYear, txtDob);

        // Row 6 labels: Email / Phone
        addLabelRow(card, gc, 6, "Email", "Phone");
        // Row 7 fields
        txtEmail = makeField(FW);
        txtPhone = makeField(FW);
        addFieldRow(card, gc, 7, txtEmail, txtPhone);

        // Row 8 label: Address (full width)
        gc.gridx = 0; gc.gridy = 8; gc.gridwidth = 2;
        gc.insets = new Insets(10, 8, 2, 8);
        card.add(UITheme.createLabel("Address", UITheme.FONT_SMALL, UITheme.TEXT_MUTED), gc);

        // Row 9 field: Address (full width)
        gc.gridy  = 9;
        gc.insets = new Insets(0, 8, 8, 8);
        txtAddress = makeField(FW * 2 + 20);
        card.add(txtAddress, gc);
        gc.gridwidth = 1; // reset

        // Row 10: Divider
        gc.gridx = 0; gc.gridy = 10; gc.gridwidth = 2;
        gc.insets = new Insets(6, 8, 10, 8);
        card.add(UITheme.createDivider(), gc);

        // Row 11: Buttons
        gc.gridy = 11;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnClear  = UITheme.createButton("\u21BA  Clear",  UITheme.TEXT_MUTED);
        JButton btnCancel = UITheme.createButton("\u2717  Cancel", UITheme.DANGER);
        JButton btnSave   = UITheme.createButton(
            existing == null ? "\u2713  Save Student" : "\u2713  Update Student",
            UITheme.SUCCESS);

        btnClear .setPreferredSize(new Dimension(110, 36));
        btnCancel.setPreferredSize(new Dimension(110, 36));
        btnSave  .setPreferredSize(new Dimension(150, 36));

        btnPanel.add(btnClear);
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        card.add(btnPanel, gc);

        root.add(card, BorderLayout.CENTER);
        setContentPane(root);

        // ── Button Actions ────────────────────────────────────
        btnSave  .addActionListener(e -> doSave());
        btnCancel.addActionListener(e -> dispose());
        btnClear .addActionListener(e -> clearFields());
    }

    // ─────────────────────────────────────────────────────────
    //  GRID HELPERS
    // ─────────────────────────────────────────────────────────

    /** Adds two labels side-by-side at the given grid row. */
    private void addLabelRow(JPanel p, GridBagConstraints gc,
                              int row, String left, String right) {
        gc.gridy   = row;
        gc.insets  = new Insets(10, 8, 2, 8);
        gc.gridwidth = 1;
        gc.gridx = 0;
        p.add(UITheme.createLabel(left,  UITheme.FONT_SMALL, UITheme.TEXT_MUTED), gc);
        gc.gridx = 1;
        p.add(UITheme.createLabel(right, UITheme.FONT_SMALL, UITheme.TEXT_MUTED), gc);
    }

    /** Adds two components side-by-side at the given grid row. */
    private void addFieldRow(JPanel p, GridBagConstraints gc,
                              int row, Component c1, Component c2) {
        gc.gridy   = row;
        gc.insets  = new Insets(0, 8, 4, 8);
        gc.gridwidth = 1;
        gc.gridx = 0; p.add(c1, gc);
        gc.gridx = 1; p.add(c2, gc);
    }

    /** Creates a styled text field with fixed preferred width. */
    private JTextField makeField(int width) {
        JTextField tf = UITheme.createTextField(20);
        tf.setPreferredSize(new Dimension(width, 36));
        return tf;
    }

    // ─────────────────────────────────────────────────────────
    //  DATA HELPERS
    // ─────────────────────────────────────────────────────────

    /** Pre-fills all fields from the existing Student (Edit mode). */
    private void fillFields(Student s) {
        txtFirstName.setText(s.getFirstName());
        txtLastName .setText(s.getLastName());
        txtRollNo   .setText(s.getRollNumber());
        cbDept      .setSelectedItem(s.getDepartment());
        cbYear      .setSelectedItem(String.valueOf(s.getYear()));
        txtEmail    .setText(s.getEmail()   != null ? s.getEmail()   : "");
        txtPhone    .setText(s.getPhone()   != null ? s.getPhone()   : "");
        txtDob      .setText(s.getDob()     != null ? s.getDob()     : "");
        txtAddress  .setText(s.getAddress() != null ? s.getAddress() : "");
    }

    /** Clears all input fields back to defaults. */
    private void clearFields() {
        txtFirstName.setText("");
        txtLastName .setText("");
        txtRollNo   .setText("");
        txtEmail    .setText("");
        txtPhone    .setText("");
        txtDob      .setText("");
        txtAddress  .setText("");
        cbDept.setSelectedIndex(0);
        cbYear.setSelectedIndex(0);
        txtFirstName.requestFocus();
    }

    // ─────────────────────────────────────────────────────────
    //  SAVE / UPDATE
    // ─────────────────────────────────────────────────────────

    /**
     * Reads form, validates, then calls DAO to INSERT or UPDATE.
     * On success: fires onSuccess callback and closes dialog.
     * On failure: shows error message and stays open.
     */
    private void doSave() {
        // Read all field values
        String firstName = txtFirstName.getText().trim();
        String lastName  = txtLastName .getText().trim();
        String rollNo    = txtRollNo   .getText().trim();
        String dept      = (String) cbDept.getSelectedItem();
        String yearStr   = (String) cbYear.getSelectedItem();
        String email     = txtEmail  .getText().trim();
        String phone     = txtPhone  .getText().trim();
        String dob       = txtDob    .getText().trim();
        String address   = txtAddress.getText().trim();

        // Get parent JFrame for validation popups
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Run validation
        if (!Validator.validateStudentForm(
                parent, firstName, lastName, rollNo, dept, yearStr, email, phone)) {
            return; // validation failed; popup already shown by Validator
        }

        // Build Student object
        Student s = new Student(
            firstName, lastName, rollNo, dept,
            Integer.parseInt(yearStr),
            email, phone, dob, address
        );

        boolean success;

        if (existing == null) {
            // ── ADD mode ──────────────────────────────────────
            success = dao.addStudent(s);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Student added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // ── EDIT mode ─────────────────────────────────────
            s.setStudentId(existing.getStudentId()); // preserve the ID
            success = dao.updateStudent(s);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Student updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (!success) {
            JOptionPane.showMessageDialog(this,
                "Operation failed.\nRoll number may already exist, or DB error.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        onSuccess.run(); // tell parent to refresh table
        dispose();       // close dialog
    }
}
