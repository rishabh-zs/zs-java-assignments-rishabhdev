package com.zs.assignment7.repositories;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.zip.GZIPOutputStream;
import com.zs.assignment7.utils.DatabaseConnectionManager;

/**
 * The type File export repository.
 */
public class FileExportRepository {

    /**
     * Export data to compressed file.
     *
     * @param filePath the file path
     */
    public void exportDataToCompressedFile(String filePath) {
        System.out.println("Starting extraction to compressed file: " + filePath);

        String sql = "SELECT s.id, s.first_name, s.last_name, d.name AS department_name " +
                "FROM students s " +
                "JOIN student_dept_mapping sd ON s.id = sd.student_id " +
                "JOIN departments d ON sd.dept_id = d.id";

        try (Connection conn = DatabaseConnectionManager.Connect();) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setFetchSize(10000);

                try (ResultSet rs = stmt.executeQuery();
                     FileOutputStream fos = new FileOutputStream(filePath);
                     GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
                     OutputStreamWriter osw = new OutputStreamWriter(gzipOS, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(osw)) {

                    writer.write("ID,FirstName,LastName,Department\n");

                    int rowCount = 0;
                    while (rs.next()) {
                        writer.write(rs.getInt("id") + "," +
                                rs.getString("first_name") + "," +
                                rs.getString("last_name") + "," +
                                rs.getString("department_name") + "\n");
                        rowCount++;
                    }
                    System.out.println("Extraction complete. " + rowCount + " rows written and compressed.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error during extraction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
