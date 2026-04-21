package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import com.sms.util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MainFrame.java — Main Application Window
 * ─────────────────────────────────────────────────────────────
 * Contains:
 *   • Left sidebar with navigation buttons
 *   • Top bar with title and logout
 *   • Center content area (swappable panels):
 *       - Dashboard panel  (stat cards + recent table)
 *       - View panel       (full table with Edit/Delete)
 *       - Search panel     (search bar + results table)
 *
 * Add Student and Edit Student open as modal dialogs
 * (AddEditStudentDialog.java).
 * ─────────────────────────────────────────────────────────────
 */
public class MainFrame extends JFrame {

    // ── DAO ───────────────────────────────────────────────────
    private StudentDAO dao;

    // ── Table ─────────────────────────────────────────────────
    private JTable            table;
    private DefaultTableModel tableModel;

    // ── Sidebar buttons ───────────────────────────────────────
    private JButton btnDashboard;
    private JButton btnAdd;
    private JButton btnView;
    private JButton btnSearch;
    private JButton btnLogout;

    // ── Swappable center panel ────────────────────────────────
    private JPanel contentArea;

    // ── Search field (kept for reuse) ─────────────────────────
    private JTextField txtSearch;

    // ─────────────────────────────────────────────────────────
    public MainFrame() {
        dao = new StudentDAO();
        initUI();
    }

    // ─────────────────────────────────────────────────────────
    //  UI SETUP
    // ─────────────────────────────────────────────────────────
    private void initUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildTopBar(),  BorderLayout.NORTH);

        contentArea = buildDashboardPanel();
        add(contentArea, BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────────────────
    //  SIDEBAR
    // ─────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Logo
        JLabel logo = new JLabel("  \uD83D\uDCCB  SMS", SwingConstants.LEFT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setMaximumSize(new Dimension(200, 50));
        logo.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 0));
        sidebar.add(logo);
        sidebar.add(makeSidebarDivider());

        // Build sidebar nav buttons
        btnDashboard = makeSidebarBtn("\uD83C\uDFE0   Dashboard");
        btnAdd       = makeSidebarBtn("\u2795   Add Student");
        btnView      = makeSidebarBtn("\uD83D\uDC41   View Students");
        btnSearch    = makeSidebarBtn("\uD83D\uDD0D   Search");
        btnLogout    = makeSidebarBtn("\uD83D\uDEAA   Logout");

        // Default active
        setSidebarActive(btnDashboard);

        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(btnDashboard);
        sidebar.add(btnAdd);
        sidebar.add(btnView);
        sidebar.add(btnSearch);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSidebarDivider());
        sidebar.add(btnLogout);

        // ── Sidebar button actions ─────────────────────────────
        btnDashboard.addActionListener(e ->
            switchContent(buildDashboardPanel(), btnDashboard));

        btnAdd.addActionListener(e -> openAddStudentDialog());

        btnView.addActionListener(e ->
            switchContent(buildViewPanel(), btnView));

        btnSearch.addActionListener(e ->
            switchContent(buildSearchPanel(), btnSearch));

        btnLogout.addActionListener(e -> doLogout());

        return sidebar;
    }

    /**
     * Creates a single sidebar navigation button.
     */
    private JButton makeSidebarBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_SIDEBAR);
        btn.setForeground(new Color(200, 215, 240));
        btn.setBackground(UITheme.SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 10));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 48));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(UITheme.SIDEBAR_ACTIVE))
                    btn.setBackground(new Color(40, 85, 140));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(UITheme.SIDEBAR_ACTIVE))
                    btn.setBackground(UITheme.SIDEBAR_BG);
            }
        });
        return btn;
    }

    /**
     * Highlights the active sidebar button and resets others.
     */
    private void setSidebarActive(JButton active) {
        JButton[] all = { btnDashboard, btnAdd, btnView, btnSearch, btnLogout };
        for (JButton b : all) {
            if (b == null) continue;
            boolean isActive = (b == active);
            b.setBackground(isActive ? UITheme.SIDEBAR_ACTIVE : UITheme.SIDEBAR_BG);
            b.setForeground(isActive ? Color.WHITE : new Color(200, 215, 240));
        }
    }

    private Component makeSidebarDivider() {
        JPanel d = new JPanel();
        d.setBackground(new Color(50, 90, 140));
        d.setMaximumSize(new Dimension(200, 1));
        d.setPreferredSize(new Dimension(200, 1));
        return d;
    }

    // ─────────────────────────────────────────────────────────
    //  TOP BAR
    // ─────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel title = UITheme.createLabel(
            "Student Management System", UITheme.FONT_HEADING, UITheme.PRIMARY);

        JLabel userBtn = UITheme.createLabel(
            "Admin  |  Logout", UITheme.FONT_SMALL, UITheme.PRIMARY_LIGHT);
        userBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { doLogout(); }
        });

        top.add(title,   BorderLayout.WEST);
        top.add(userBtn, BorderLayout.EAST);
        return top;
    }

    // ─────────────────────────────────────────────────────────
    //  SWITCH CONTENT PANEL
    // ─────────────────────────────────────────────────────────
    /**
     * Replaces the center panel with a new one and highlights
     * the correct sidebar button.
     */
    private void switchContent(JPanel newPanel, JButton activeBtn) {
        getContentPane().remove(contentArea);
        contentArea = newPanel;
        getContentPane().add(contentArea, BorderLayout.CENTER);
        setSidebarActive(activeBtn);
        revalidate();
        repaint();
    }

    // ─────────────────────────────────────────────────────────
    //  DASHBOARD PANEL
    // ─────────────────────────────────────────────────────────
    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Stat cards row ────────────────────────────────────
        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsRow.setOpaque(false);
        cardsRow.add(buildStatCard("Total Students",
                String.valueOf(dao.getTotalCount()), UITheme.PRIMARY_LIGHT));
        cardsRow.add(buildStatCard("Departments", "6", UITheme.SUCCESS));
        cardsRow.add(buildStatCard("System Status", "Active", UITheme.ACCENT));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(UITheme.createLabel(
            "Dashboard", UITheme.FONT_HEADING, UITheme.PRIMARY), BorderLayout.WEST);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(heading,  BorderLayout.NORTH);
        north.add(cardsRow, BorderLayout.CENTER);
        north.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ── Recent students table ─────────────────────────────
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(UITheme.createLabel(
            "Recent Students", UITheme.FONT_SUBHEAD, UITheme.PRIMARY), BorderLayout.NORTH);

        initTableModel();
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        tableCard.add(scroll, BorderLayout.CENTER);

        loadStudentTable(dao.getAllStudents());

        panel.add(north,     BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        // Top color bar
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(card.getWidth(), 5));

        JLabel lblTitle = UITheme.createLabel(title, UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        JLabel lblValue = UITheme.createLabel(value,
                new Font("Segoe UI", Font.BOLD, 30), color);

        JPanel inner = new JPanel(new GridLayout(2, 1, 0, 4));
        inner.setOpaque(false);
        inner.add(lblTitle);
        inner.add(lblValue);

        card.add(bar,   BorderLayout.NORTH);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // ─────────────────────────────────────────────────────────
    //  VIEW PANEL  (with Edit / Delete buttons)
    // ─────────────────────────────────────────────────────────
    private JPanel buildViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top row: title + Add button
        JPanel topRow = new JPanel(new BorderLayout(10, 0));
        topRow.setOpaque(false);
        topRow.add(UITheme.createLabel(
            "All Students", UITheme.FONT_HEADING, UITheme.PRIMARY), BorderLayout.WEST);

        JButton btnAddNew = UITheme.createButton("+ Add New", UITheme.SUCCESS);
        btnAddNew.setPreferredSize(new Dimension(110, 34));
        btnAddNew.addActionListener(e -> openAddStudentDialog());
        topRow.add(btnAddNew, BorderLayout.EAST);

        // Table card
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 10));

        initTableModel();
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        // Edit / Delete buttons
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionRow.setOpaque(false);
        JButton btnEdit   = UITheme.createButton("\u270F  Edit Selected",   UITheme.WARNING);
        JButton btnDelete = UITheme.createButton("\uD83D\uDDD1  Delete Selected", UITheme.DANGER);
        btnEdit  .setPreferredSize(new Dimension(150, 34));
        btnDelete.setPreferredSize(new Dimension(160, 34));
        actionRow.add(btnEdit);
        actionRow.add(btnDelete);
        card.add(actionRow, BorderLayout.SOUTH);

        loadStudentTable(dao.getAllStudents());

        // ── Edit action ───────────────────────────────────────
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this,
                    "Please select a student row to edit.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            Student s = dao.getStudentById(id);
            if (s != null) {
                new AddEditStudentDialog(this, dao, s, () ->
                    switchContent(buildViewPanel(), btnView)
                ).setVisible(true);
            }
        });

        // ── Delete action ─────────────────────────────────────
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this,
                    "Please select a student row to delete.");
                return;
            }
            int id   = (int) tableModel.getValueAt(row, 0);
            String name = tableModel.getValueAt(row, 2)
                          + " " + tableModel.getValueAt(row, 3);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student: " + name + "?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(this,
                        "Student deleted successfully.");
                    loadStudentTable(dao.getAllStudents());
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Delete failed. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(card,   BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────
    //  SEARCH PANEL
    // ─────────────────────────────────────────────────────────
    private JPanel buildSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(UITheme.createLabel(
            "Search Students", UITheme.FONT_HEADING, UITheme.PRIMARY), BorderLayout.NORTH);

        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 14));

        // Search bar
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setOpaque(false);
        txtSearch = UITheme.createTextField(30);
        txtSearch.setPreferredSize(new Dimension(400, 36));

        JButton btnGo  = UITheme.createButton("Search", UITheme.PRIMARY_LIGHT);
        JButton btnAll = UITheme.createButton("Show All", UITheme.TEXT_MUTED);
        btnGo .setPreferredSize(new Dimension(100, 36));
        btnAll.setPreferredSize(new Dimension(100, 36));

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnBox.setOpaque(false);
        btnBox.add(btnGo);
        btnBox.add(btnAll);

        searchBar.add(txtSearch, BorderLayout.CENTER);
        searchBar.add(btnBox,    BorderLayout.EAST);
        card.add(searchBar, BorderLayout.NORTH);

        // Result table
        initTableModel();
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        loadStudentTable(dao.getAllStudents());

        // ── Actions ───────────────────────────────────────────
        btnGo.addActionListener(e -> doSearch());
        btnAll.addActionListener(e -> {
            txtSearch.setText("");
            loadStudentTable(dao.getAllStudents());
        });
        // Press Enter in search box
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doSearch();
            }
        });

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) {
            loadStudentTable(dao.getAllStudents());
            return;
        }
        List<Student> results = dao.searchStudents(kw);
        loadStudentTable(results);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No students found for: \"" + kw + "\"",
                "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────────────────
    //  TABLE HELPERS
    // ─────────────────────────────────────────────────────────
    /**
     * Initializes (or re-initializes) the table model with column headers.
     * Must be called before creating a new JTable.
     */
    private void initTableModel() {
        String[] columns = {
            "ID", "Roll No", "First Name", "Last Name",
            "Department", "Year", "Email", "Phone"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // table is read-only; use Edit button
            }
        };
    }

    /**
     * Clears the table and fills it with the given student list.
     * Called after every DB operation to keep UI in sync.
     */
    public void loadStudentTable(List<Student> students) {
        if (tableModel == null) initTableModel();
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getRollNumber(),
                s.getFirstName(),
                s.getLastName(),
                s.getDepartment(),
                s.getYear(),
                s.getEmail(),
                s.getPhone()
            });
        }
    }

    // ─────────────────────────────────────────────────────────
    //  DIALOGS & ACTIONS
    // ─────────────────────────────────────────────────────────
    private void openAddStudentDialog() {
        // Pass null for Student = Add mode
        new AddEditStudentDialog(this, dao, null, () ->
            loadStudentTable(dao.getAllStudents())
        ).setVisible(true);
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}
