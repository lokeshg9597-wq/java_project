
package com.scholarship.dao;

import com.scholarship.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // Submit scholarship application
    public String applyScholarship(
            int studentId,
            int scholarshipId,
            double marks) {

        String checkSql =
                "SELECT s.family_income, sc.income_limit, " +
                        "sc.minimum_marks " +
                        "FROM students s CROSS JOIN scholarships sc " +
                        "WHERE s.student_id = ? " +
                        "AND sc.scholarship_id = ?";

        String insertSql =
                "INSERT INTO applications " +
                        "(student_id, scholarship_id, marks, application_date, status) " +
                        "VALUES (?, ?, ?, CURDATE(), 'PENDING')";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return "Database connection failed!";
            }

            try (PreparedStatement check =
                         con.prepareStatement(checkSql)) {

                check.setInt(1, studentId);
                check.setInt(2, scholarshipId);

                try (ResultSet rs = check.executeQuery()) {

                    if (!rs.next()) {
                        return "Student or scholarship not found!";
                    }

                    double familyIncome =
                            rs.getDouble("family_income");

                    double incomeLimit =
                            rs.getDouble("income_limit");

                    double minimumMarks =
                            rs.getDouble("minimum_marks");

                    if (marks < 0 || marks > 100) {
                        return "Marks must be between 0 and 100!";
                    }

                    if (familyIncome > incomeLimit) {
                        return "Not eligible: Family income exceeds the limit.";
                    }

                    if (marks < minimumMarks) {
                        return "Not eligible: Minimum marks requirement not met.";
                    }
                }
            }

            try (PreparedStatement insert =
                         con.prepareStatement(insertSql)) {

                insert.setInt(1, studentId);
                insert.setInt(2, scholarshipId);
                insert.setDouble(3, marks);

                int result = insert.executeUpdate();

                if (result > 0) {
                    return "SUCCESS";
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            return "You have already applied for this scholarship!";

        } catch (SQLException e) {

            System.out.println("Application Error: " + e.getMessage());

            return "Application failed. Please try again.";
        }

        return "Application failed!";
    }

    // Retrieve applications belonging to a student
    public List<String[]> getStudentApplications(int studentId) {

        List<String[]> applications = new ArrayList<>();

        String sql =
                "SELECT a.application_id, sc.scholarship_name, " +
                        "a.marks, a.application_date, a.status, a.remarks " +
                        "FROM applications a " +
                        "JOIN scholarships sc " +
                        "ON a.scholarship_id = sc.scholarship_id " +
                        "WHERE a.student_id = ? " +
                        "ORDER BY a.application_id DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return applications;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        String[] data = {
                                String.valueOf(rs.getInt("application_id")),
                                rs.getString("scholarship_name"),
                                String.valueOf(rs.getDouble("marks")),
                                String.valueOf(rs.getDate("application_date")),
                                rs.getString("status"),
                                rs.getString("remarks")
                        };

                        applications.add(data);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Application Retrieval Error: " + e.getMessage());
        }

        return applications;
    }
}
