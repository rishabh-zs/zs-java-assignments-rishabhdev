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
    private final DataGenerationService dataGenService = new DataGenerationService();
    private final DatabaseService dbService = new DatabaseService();
    private final ExportService exportService = new ExportService();

    /**
     * Execute assignment.
     */
    public void executeAssignment() {
        try {
            System.out.println("Starting Assignment 7 execution...");
            // 1. Setup DB Schema
            dbService.setupSchema();

            // 2. Generate 1M Records in memory
            List<Student> students = dataGenService.generateStudents(1_000_000);

            // 3. Load records into DB
            dbService.batchInsertStudents(students);

            // 4. Map students to departments randomly
            dbService.assignRandomDepartments();

            // 5. Extract and compress to file
            exportService.exportDataCompressed(dbService.getConnection(), "students_data.csv.gz");

            System.out.println("Assignment 7 execution finished successfully!");

        } catch (Exception e) {
            System.err.println("An error occurred during execution:");
            e.printStackTrace();
        }
    }
}