
package com.scholarship;

import com.scholarship.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminSetup {

    public static void main(String[] args) {

        String username = "admin";
        String password = "Admin@123";

        String sql = "INSERT INTO admins (username, password_hash) " +
                "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("Database unavailable!");
                return;
            }

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, hashedPassword);

                ps.executeUpdate();

                System.out.println("Admin Account Created!");
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }

        } catch (Exception e) {
            System.out.println(
                    "Admin setup error: " + e.getMessage()
            );
        }
    }
}
