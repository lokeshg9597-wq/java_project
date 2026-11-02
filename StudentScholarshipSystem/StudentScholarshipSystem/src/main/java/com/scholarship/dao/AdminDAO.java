
package com.scholarship.dao;

import com.scholarship.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // 1. View all students
    public List<String[]> getAllStudents() {

        List<String[]> students = new ArrayList<>();

        String sql = "SELECT student_id, name, register_no, " +
                "department, year, email, family_income FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                students.add(new String[]{
                        String.valueOf(rs.getInt("student_id")),
                        rs.getString("name"),
                        rs.getString("register_no"),
                        rs.getString("department"),
                        String.valueOf(rs.getInt("year")),
                        rs.getString("email"),
                        String.valueOf(rs.getDouble("family_income"))
                });
            }

        } catch (SQLException e) {
            System.out.println("Student retrieval error: " + e.getMessage());
        }

        return students;
    }

    // 2. View all applications
    public List<String[]> getAllApplications() {

        List<String[]> applications = new ArrayList<>();

        String sql = "SELECT a.application_id, s.name, " +
                "sc.scholarship_name, a.marks, " +
                "a.application_date, a.status, a.remarks " +
                "FROM applications a " +
                "JOIN students s ON a.student_id = s.student_id " +
                "JOIN scholarships sc ON a.scholarship_id = sc.scholarship_id " +
                "ORDER BY a.application_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                applications.add(new String[]{
                        String.valueOf(rs.getInt("application_id")),
                        rs.getString("name"),
                        rs.getString("scholarship_name"),
                        String.valueOf(rs.getDouble("marks")),
                        String.valueOf(rs.getDate("application_date")),
                        rs.getString("status"),
                        rs.getString("remarks")
                });
            }

        } catch (SQLException e) {
            System.out.println("Application retrieval error: " + e.getMessage());
        }

        return applications;
    }

    // 3. Approve or reject an application
    public boolean updateApplicationStatus(
            int applicationId,
            String status,
            String remarks) {

        if (!status.equals("APPROVED") &&
                !status.equals("REJECTED")) {
            return false;
        }

        String sql = "UPDATE applications " +
                "SET status = ?, remarks = ? " +
                "WHERE application_id = ? AND status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, applicationId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Status update error: " + e.getMessage());
            return false;
        }
    }

    // 4. Add scholarship
    public boolean addScholarship(
            String name,
            String description,
            double incomeLimit,
            double minimumMarks,
            double amount) {

        String sql = "INSERT INTO scholarships " +
                "(scholarship_name, description, income_limit, minimum_marks, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, incomeLimit);
            ps.setDouble(4, minimumMarks);
            ps.setDouble(5, amount);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add scholarship error: " + e.getMessage());
            return false;
        }
    }

    // 5. Update scholarship
    public boolean updateScholarship(
            int id,
            String name,
            String description,
            double incomeLimit,
            double minimumMarks,
            double amount) {

        String sql = "UPDATE scholarships SET " +
                "scholarship_name = ?, description = ?, " +
                "income_limit = ?, minimum_marks = ?, amount = ? " +
                "WHERE scholarship_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, incomeLimit);
            ps.setDouble(4, minimumMarks);
            ps.setDouble(5, amount);
            ps.setInt(6, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update scholarship error: " + e.getMessage());
            return false;
        }
    }

    // 6. Delete scholarship
    public boolean deleteScholarship(int id) {

        String sql = "DELETE FROM scholarships WHERE scholarship_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Delete failed. This scholarship may have applications: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public boolean loginAdmin(String username, String password) {

        String sql = "SELECT password_hash FROM admins WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return com.scholarship.util.PasswordUtil.verifyPassword(
                            password,
                            rs.getString("password_hash")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Admin Login Error: " + e.getMessage());
        }

        return false;
    }
}
