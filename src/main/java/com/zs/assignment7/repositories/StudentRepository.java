package com.zs.assignment7.repositories;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class StudentRepository {

    public void createStudentSchema() {
        String createStudents = "CREATE TABLE IF NOT EXISTS students (" +
                "id SERIAL PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "mobile VARCHAR(15))";

        try (Connection conn = DatabaseConnectionManager.Connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS students");

            stmt.execute(createStudents);

            System.out.println("Student Schema created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
}
