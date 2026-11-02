
package com.scholarship;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/scholarship_db";

    private static final String USER = "root";

    // Default XAMPP MySQL root password is usually empty
    private static final String PASSWORD = "";

    public static Connection getConnection() {

        try {
            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            System.out.println(
                    "XAMPP MySQL Connected Successfully!"
            );

            return con;

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}
