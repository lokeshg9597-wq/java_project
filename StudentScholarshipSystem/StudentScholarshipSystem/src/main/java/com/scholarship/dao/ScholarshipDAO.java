
package com.scholarship.dao;

import com.scholarship.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipDAO {

    // Display all scholarships
    public List<String[]> getAllScholarships() {

        List<String[]> scholarships = new ArrayList<>();

        String sql = "SELECT scholarship_id, scholarship_name, " +
                "description, income_limit, minimum_marks, amount " +
                "FROM scholarships";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                scholarships.add(new String[]{
                        String.valueOf(rs.getInt("scholarship_id")),
                        rs.getString("scholarship_name"),
                        rs.getString("description"),
                        String.valueOf(rs.getDouble("income_limit")),
                        String.valueOf(rs.getDouble("minimum_marks")),
                        String.valueOf(rs.getDouble("amount"))
                });
            }

        } catch (SQLException e) {
            System.out.println("Scholarship Error: " + e.getMessage());
        }

        return scholarships;
    }
}
