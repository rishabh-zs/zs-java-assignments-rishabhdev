package com.zs.assignment7.services;

import java.io.*;
import java.sql.*;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class ExportService {
    public void exportDataCompressed(Connection conn, String fileName) {
        String query = "SELECT s.id, s.first_name, s.last_name, d.dept_name " +
                "FROM students s " +
                "JOIN student_dept_mapping m ON s.id = m.student_id " +
                "JOIN departments d ON d.id = m.dept_id";

        System.out.println("Extracting and compressing data to " + fileName + "...");

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileOutputStream fos = new FileOutputStream(fileName);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             PrintWriter writer = new PrintWriter(gzos)) {

            writer.println("ID,First_Name,Last_Name,Department");
            while (rs.next()) {
                writer.printf("%d,%s,%s,%s%n",
                        rs.getInt("id"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("dept_name"));
            }
            System.out.println("Data extraction and compression complete.");

        } catch (SQLException | IOException e) {
            System.err.println("Error during export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
