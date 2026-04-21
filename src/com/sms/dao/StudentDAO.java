package com.sms.dao;

import com.sms.db.DBConnection;
import com.sms.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO.java — Data Access Object
 * ─────────────────────────────────────────────────────────────
 * All database operations for the Student entity.
 *
 * Methods:
 *   addStudent(Student)       → INSERT
 *   getAllStudents()           → SELECT *
 *   getStudentById(int)       → SELECT by ID
 *   searchStudents(String)    → LIKE search
 *   updateStudent(Student)    → UPDATE
 *   deleteStudent(int)        → DELETE
 *   getTotalCount()           → COUNT(*)
 * ─────────────────────────────────────────────────────────────
 */
public class StudentDAO {

    // ── CREATE ────────────────────────────────────────────────
    /**
     * Inserts a new student into the database.
     * @param s Student object (studentId is ignored — DB auto-assigns it)
     * @return true if insert was successful
     */
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students " +
                     "(first_name, last_name, roll_number, department, year, email, phone, dob, address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getRollNumber());
            ps.setString(4, s.getDepartment());
            ps.setInt   (5, s.getYear());
            ps.setString(6, s.getEmail());
            ps.setString(7, s.getPhone());
            ps.setString(8, s.getDob());
            ps.setString(9, s.getAddress());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("addStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ALL ──────────────────────────────────────────────
    /**
     * Returns all students ordered by newest first.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id DESC";
        try {
            Statement st = DBConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("getAllStudents error: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY ID ────────────────────────────────────────────
    /**
     * Finds a single student by their primary key ID.
     * Used to pre-fill the Edit dialog.
     */
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student s = mapRow(rs);
                rs.close(); ps.close();
                return s;
            }
        } catch (SQLException e) {
            System.err.println("getStudentById error: " + e.getMessage());
        }
        return null;
    }

    // ── SEARCH ────────────────────────────────────────────────
    /**
     * Searches students by keyword across multiple columns.
     * @param keyword the search term (partial match supported)
     */
    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                     "first_name  LIKE ? OR " +
                     "last_name   LIKE ? OR " +
                     "roll_number LIKE ? OR " +
                     "department  LIKE ? OR " +
                     "email       LIKE ?";
        String kw = "%" + keyword + "%";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ps.setString(5, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            rs.close(); ps.close();
        } catch (SQLException e) {
            System.err.println("searchStudents error: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────
    /**
     * Updates an existing student's details.
     * Matches by studentId.
     */
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET " +
                     "first_name=?, last_name=?, roll_number=?, department=?, " +
                     "year=?, email=?, phone=?, dob=?, address=? " +
                     "WHERE student_id=?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1,  s.getFirstName());
            ps.setString(2,  s.getLastName());
            ps.setString(3,  s.getRollNumber());
            ps.setString(4,  s.getDepartment());
            ps.setInt   (5,  s.getYear());
            ps.setString(6,  s.getEmail());
            ps.setString(7,  s.getPhone());
            ps.setString(8,  s.getDob());
            ps.setString(9,  s.getAddress());
            ps.setInt   (10, s.getStudentId());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────
    /**
     * Deletes a student by ID.
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("deleteStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── COUNT ─────────────────────────────────────────────────
    /**
     * Returns total number of students in the database.
     * Used by the Dashboard stat card.
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM students";
        try {
            Statement st = DBConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close(); st.close();
                return count;
            }
        } catch (SQLException e) {
            System.err.println("getTotalCount error: " + e.getMessage());
        }
        return 0;
    }

    // ── PRIVATE HELPER ────────────────────────────────────────
    /**
     * Maps a ResultSet row to a Student object.
     * Called internally by all SELECT methods.
     */
    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt   ("student_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("roll_number"),
            rs.getString("department"),
            rs.getInt   ("year"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("dob"),
            rs.getString("address")
        );
    }
}
