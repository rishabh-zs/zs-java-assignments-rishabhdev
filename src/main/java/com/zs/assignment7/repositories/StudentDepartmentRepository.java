package com.zs.assignment7.repositories;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.models.StudentDepartmentMapping;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * The type Assignment repository.
 */
public class StudentDepartmentRepository {

    /**
     * Create schema.
     */
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

        try (Connection conn = DatabaseConnectionManager.Connect();
             Statement stmt = conn.createStatement()) {

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

    /**
     * Insert departments.
     */
    public void insertDepartments(List<Department> departments) {
        String sql = "INSERT INTO departments (id, name) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnectionManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Department department : departments) {
                pstmt.setInt(1, department.getId());
                pstmt.setString(2, department.getName());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("----Departments inserted----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Batch insert students.
     *
     * @param students the students
     */
    public void batchInsertStudents(List<Student> students) {
        String sql = "INSERT INTO students (id, first_name, last_name, mobile) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.Connect();) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int count = 0;
                for (Student s : students) {
                    pstmt.setInt(1, s.getId());
                    pstmt.setString(2, s.getFirstName());
                    pstmt.setString(3, s.getLastName());
                    pstmt.setString(4, s.getMobile());
                    pstmt.addBatch();

                    if (++count % 50000 == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println("Inserted " + count + " records...");
                    }
                }
                pstmt.executeBatch();
                conn.commit();
            }
            conn.setAutoCommit(true);
            System.out.println("----1 Million students inserted successfully----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Map students to departments randomly.
     */
    public void mapStudentsToDepartmentsRandomly(List<Student> students, List<Department> departments) {
        String sql = "INSERT INTO student_dept_mapping (dept_id, student_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnectionManager.Connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            Random random = new Random();
            System.out.println("----Mapping students to departments randomly in DB----");
            int count = 0;
            for (Student student : students) {
                Department department = departments.get(random.nextInt(departments.size()));
                StudentDepartmentMapping mapping = new StudentDepartmentMapping(student.getId(), department.getId());
                pstmt.setInt(1, mapping.getDept_id());
                pstmt.setInt(2, mapping.getStudent_id());
                pstmt.addBatch();

                if (++count % 50000 == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
            pstmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("----Mapping complete----");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets export connection.
     *
     * @return the export connection
     * @throws SQLException the SQL exception
     */
    public Connection getExportConnection() throws SQLException {
        return DatabaseConnectionManager.Connect();
    }
}
