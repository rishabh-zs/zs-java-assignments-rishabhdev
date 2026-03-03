package com.zs.assignment7.services;

import com.zs.assignment7.models.Student;
import java.sql.*;
import java.util.List;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class DatabaseService {
    // Ensure these match your Docker/Rancher setup
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "User#2026";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void setupSchema() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            System.out.println("Setting up database schema...");
            String dropMappingTableSql = "DROP TABLE IF EXISTS student_dept_mapping;";
            String dropStudentsTableSql = "DROP TABLE IF EXISTS students;";
            String dropDepartmentsTableSql = "DROP TABLE IF EXISTS departments;";
            String createStudentsTableSql = "CREATE TABLE students (id SERIAL PRIMARY KEY, first_name VARCHAR(50), last_name VARCHAR(50), mobile VARCHAR(15));";
            String createDepartmentsTableSql = "CREATE TABLE departments (id SERIAL PRIMARY KEY, dept_name VARCHAR(10));";
            String createStudentDeptMappingTableSql = "CREATE TABLE student_dept_mapping (student_id INT REFERENCES students(id), dept_id INT REFERENCES departments(id));";
            String seedDepartmentsSql = "INSERT INTO departments (dept_name) VALUES ('CS'), ('EE'), ('Mech');";

            stmt.execute(dropMappingTableSql);
            stmt.execute(dropStudentsTableSql);
            stmt.execute(dropDepartmentsTableSql);
            stmt.execute(createStudentsTableSql);
            stmt.execute(createDepartmentsTableSql);
            stmt.execute(createStudentDeptMappingTableSql);
            stmt.execute(seedDepartmentsSql);
            System.out.println("Schema setup complete.");
        }
    }

    public void batchInsertStudents(List<Student> students) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, mobile) VALUES (?, ?, ?)";
        System.out.println("Starting batch insert of " + students.size() + " records...");

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            int count = 0;
            for (Student s : students) {
                pstmt.setString(1, s.getFirstName());
                pstmt.setString(2, s.getLastName());
                pstmt.setString(3, s.getMobile());
                pstmt.addBatch();

                if (++count % 10000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // Insert remaining
            conn.commit();
            System.out.println("1 Million students inserted successfully.");
        }
    }

    public void assignRandomDepartments() throws SQLException {
        String sql = "INSERT INTO student_dept_mapping (student_id, dept_id) " +
                "SELECT id, (floor(random() * 3) + 1) FROM students;";
        System.out.println("Assigning random departments to students...");

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            System.out.println("Random departments assigned to " + rows + " students.");
        }
    }
}
