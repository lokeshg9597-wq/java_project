
package com.scholarship;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionTest {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {

            try {

                System.out.println(
                        "Connection Test Passed!"
                );

                // Create SQL statement
                Statement stmt = con.createStatement();

                // Check selected database
                ResultSet rs = stmt.executeQuery(
                        "SELECT DATABASE()"
                );

                if (rs.next()) {

                    System.out.println(
                            "Connected Database: " +
                                    rs.getString(1)
                    );
                }

                rs.close();
                stmt.close();

                con.close();

                System.out.println(
                        "Connection Closed Successfully!"
                );

            } catch (Exception e) {

                System.out.println(e.getMessage());

            }

        } else {

            System.out.println(
                    "Please Check MySQL Configuration!"
            );
        }
    }
}
