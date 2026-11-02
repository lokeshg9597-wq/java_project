
package com.scholarship.dao;

import com.scholarship.DBConnection;
import com.scholarship.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDAO {

    // STUDENT REGISTRATION
    public boolean registerStudent(
            String name,
            String registerNo,
            String department,
            int year,
            String email,
            double familyIncome,
            String password) {

        String sql = "INSERT INTO students " +
                "(name, register_no, department, year, email, family_income, password_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, registerNo);
                ps.setString(3, department);
                ps.setInt(4, year);
                ps.setString(5, email);
                ps.setDouble(6, familyIncome);
                ps.setString(7, hashedPassword);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("Registration Error: " + e.getMessage());
            return false;
        }
    }

    // STUDENT LOGIN
    public int loginStudent(String email, String password) {

        String sql = "SELECT student_id, password_hash " +
                "FROM students WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return -1;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String storedHash =
                                rs.getString("password_hash");

                        boolean valid =
                                PasswordUtil.verifyPassword(
                                        password, storedHash
                                );

                        if (valid) {
                            return rs.getInt("student_id");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }

        return -1;
    }
}
