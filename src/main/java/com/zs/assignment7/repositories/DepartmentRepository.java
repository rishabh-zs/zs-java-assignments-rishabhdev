package com.zs.assignment7.repositories;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * The type Department repository.
 */
public class DepartmentRepository {


    /**
     * Create department schema.
     */
    public void createDepartmentSchema() {
        String createDepartments = "CREATE TABLE IF NOT EXISTS departments (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50))";

        try (Connection conn = DatabaseConnectionManager.Connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS departments");

            stmt.execute(createDepartments);
            System.out.println("Department Schema created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert departments.
     *
     * @param departments the departments
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
}
