
package com.scholarship;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ScholarshipTest {

    public static void main(String[] args) {

        String sql = "SELECT * FROM scholarships";

        try (
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            System.out.println("SCHOLARSHIP DETAILS");
            System.out.println("------------------------------");

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getInt("scholarship_id")
                );

                System.out.println(
                        "Name: " + rs.getString("scholarship_name")
                );

                System.out.println(
                        "Amount: Rs. " + rs.getDouble("amount")
                );

                System.out.println("------------------------------");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
