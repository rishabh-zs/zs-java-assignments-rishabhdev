package com.zs.assignment7.repositories;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.models.StudentDepartmentMapping;
import com.zs.assignment7.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * The type Assignment repository.
 */
public class StudentDepartmentMappingRepository {
    /**
     * Create student department mapping schema.
     */
    public void createStudentDepartmentMappingSchema() {
        String createMapping = "CREATE TABLE IF NOT EXISTS student_dept_mapping (" +
                "student_id INT, " +
                "dept_id INT)";

        try (Connection conn = DatabaseConnectionManager.Connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS student_dept_mapping");
            stmt.execute(createMapping);
            System.out.println("Student-Department Mapping Schema created successfully.");
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

}
