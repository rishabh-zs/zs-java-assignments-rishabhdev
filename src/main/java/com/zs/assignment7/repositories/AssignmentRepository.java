package com.zs.assignment7.repositories;

import com.zs.assignment7.models.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Handles all direct database interactions.
 */
public class AssignmentRepository {

    public void createSchema() {
        String createStudents = "CREATE TABLE IF NOT EXISTS students (" +
                "id SERIAL PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "mobile VARCHAR(15))";

        String createDepartments = "CREATE TABLE IF NOT EXISTS departments (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50))";

        String createMapping = "CREATE TABLE IF NOT EXISTS student_dept_mapping (" +
                "dept_id INT REFERENCES departments(id), " +
                "student_id INT REFERENCES students(id), " +
                "PRIMARY KEY (student_id, dept_id))";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clean up existing tables for re-runs
            stmt.execute("DROP TABLE IF EXISTS student_dept_mapping");
            stmt.execute("DROP TABLE IF EXISTS departments");
            stmt.execute("DROP TABLE IF EXISTS students");

            stmt.execute(createStudents);
            stmt.execute(createDepartments);
            stmt.execute(createMapping);
            System.out.println("Schema created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDepartments() {
        String sql = "INSERT INTO departments (id, name) VALUES (1, 'CS'), (2, 'EE'), (3, 'Mech') ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("----Departments inserted----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void batchInsertStudents(List<Student> students) {
        String sql = "INSERT INTO students (id, first_name, last_name, mobile) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            // Disable auto-commit for fast batch processing
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int count = 0;
                for (Student s : students) {
                    pstmt.setInt(1, s.getId());
                    pstmt.setString(2, s.getFirstName());
                    pstmt.setString(3, s.getLastName());
                    pstmt.setString(4, s.getMobile());
                    pstmt.addBatch();

                    // Execute batch every 50,000 records to prevent memory overflow
                    if (++count % 50000 == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println("Inserted " + count + " records...");
                    }
                }
                pstmt.executeBatch(); // insert remaining records
                conn.commit();
            }
            conn.setAutoCommit(true);
            System.out.println("----1 Million students inserted successfully----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mapStudentsToDepartmentsRandomly() {
        // Performing the random mapping completely inside SQL is vastly faster
        // than fetching 1 million records into Java and mapping them.
        String sql = "INSERT INTO student_dept_mapping (dept_id,student_id) " +
                "SELECT floor(random() * 3 + 1)::int, id FROM students";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("----Mapping students to departments randomly in DB----");
            stmt.execute(sql);
            System.out.println("----Mapping complete----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getExportConnection() throws SQLException {
        return DatabaseConnectionManager.getConnection();
    }
}
