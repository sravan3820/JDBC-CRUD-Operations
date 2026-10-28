package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {

    static void main() {
        try {
            Connection cn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbc",
                    "root",
                    "root"
            );

            System.out.println("data base connected");

            Statement stmt = cn.createStatement();

            String sql = "INSERT INTO employees(name,age,gender,adhaarid) VALUES('Rakesh',35,'Male',125478512542)";
            stmt.executeUpdate(sql);

            System.out.println("employe inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}