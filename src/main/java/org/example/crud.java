package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class crud {

    // 1. Connect Java to MySQL
    public static Connection getConnection() throws SQLException {

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbc",
                    "root",
                    "root"
            );


    }

    // 2. Create the employees table
    public static void create() {

        String sql = "CREATE TABLE IF NOT EXISTS employees ("
                + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "age INT NOT NULL, "
                + "gender VARCHAR(20) NOT NULL, "
                + "adhaarid VARCHAR(12) NOT NULL UNIQUE"
                + ")";

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(sql);
            System.out.println("Employees table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // 3. Insert an employee
    public static void insert(String name, int age,
                              String gender, String adhaarid) {

        String checkSql =
                "SELECT id FROM employees WHERE adhaarid = ?";

        String insertSql =
                "INSERT INTO employees (name, age, gender, adhaarid) "
                        + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection()) {

            // Check whether this Aadhaar ID already exists
            try (PreparedStatement check =
                         con.prepareStatement(checkSql)) {

                check.setString(1, adhaarid);

                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(
                                "Employee already exists. Insert skipped."
                        );
                        return;
                    }
                }
            }

            // Insert the employee if the ID is not already present
            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, gender);
                ps.setString(4, adhaarid);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Employee inserted successfully.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error inserting employee: " + e.getMessage());
        }
    }

    // 4. Fetch and display employee records
    public static void fetch(ResultSet rs) {

        try {
            boolean found = false;

            while (rs.next()) {
                found = true;

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String adhaarid = rs.getString("adhaarid");

                // Mask Aadhaar ID; display only its last four digits
                String maskedId = "XXXX XXXX "
                        + adhaarid.substring(adhaarid.length() - 4);

                System.out.println("----------------------------");
                System.out.println("ID       : " + id);
                System.out.println("Name     : " + name);
                System.out.println("Age      : " + age);
                System.out.println("Gender   : " + gender);
                System.out.println("Aadhaar  : " + maskedId);
            }

            if (!found) {
                System.out.println("No employee records found.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }
    }

    // 5. Display all employees
    public static void getAll() {

        String sql = "SELECT * FROM employees";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nAll Employees:");
            fetch(rs);

        } catch (SQLException e) {
            System.out.println("Error retrieving employees: " + e.getMessage());
        }
    }

    // 6. Retrieve an employee using ID
    public static void getBy(int id) {

        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\nEmployee with ID " + id + ":");
                fetch(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving employee: " + e.getMessage());
        }
    }

    // 7. Delete an employee using ID
    public static void delete(int id) {

        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {

        // Create the table
        create();

        // Insert a sample employee
        // Use this fictional sample ID for testing only
        insert("Rahul", 22, "Male", "999999999999");

        // Display all employees
        getAll();

        // Find employee with ID 1
        getBy(1);

        // Optional: uncomment only when you want to delete employee ID 1
        // delete(1);
    }
}