package com.zs.assignment7.controllers;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.services.StudentService;
import com.zs.assignment7.services.StudentDepartmentMappingService;
import com.zs.assignment7.services.FileExportService;
import java.util.List;

/**
 * The type Database controller.
 */
public class DatabaseController {
    private final StudentService studentService = new StudentService();
    private final StudentDepartmentMappingService studentDepartmentMappingService = new StudentDepartmentMappingService();
    private final FileExportService fileExportService = new FileExportService();

    /**
     * Execute flow.
     */
    public void executeFlow() {
        long startTime = System.currentTimeMillis();

        List<Student> students = studentService.generateStudents(1000000);

        studentDepartmentMappingService.initializeDatabase(students);

        fileExportService.exportDataToCompressedFile("student_departments.csv.gz");

        long endTime = System.currentTimeMillis();
        System.out.println("Total Execution Time: " + (endTime - startTime) / 1000 + " seconds.");
    }
}