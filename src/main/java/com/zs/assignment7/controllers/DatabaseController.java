package com.zs.assignment7.controllers;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.services.DataGenerationService;
import com.zs.assignment7.services.DatabaseService;
import com.zs.assignment7.services.ExportService;
import java.util.List;

/**
 * The type Database controller.
 */
public class DatabaseController {
    private final DataGenerationService dataGenerationService = new DataGenerationService();
    private final DatabaseService dbService = new DatabaseService();
    private final ExportService exportService = new ExportService();

    /**
     * Execute flow.
     */
    public void executeFlow() {
        long startTime = System.currentTimeMillis();

        // 1. Generate records
        List<Student> students = dataGenerationService.callGenerateStudents(1000000);

        // 2 & 3 & 4. Load schema, departments, students, and map them
        dbService.initializeDatabase(students);

        // 5 & 6. Extract data to a size-reduced file (.gz)
        exportService.callExportDataToCompressedFile("student_departments.csv.gz");

        long endTime = System.currentTimeMillis();
        System.out.println("Total Execution Time: " + (endTime - startTime) / 1000 + " seconds.");
    }
}