package com.zs.assignment9.controller;

import com.zs.assignment9.model.Student;
import com.zs.assignment9.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Student controller.
 */
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    /**
     * Instantiates a new Student controller.
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Handle create student.
     *
     * @param firstName the first name
     * @param lastName  the last name
     */
    public void handleCreateStudent(String firstName, String lastName) {
        logger.info("--> Creating student: firstName='{}', lastName='{}'", firstName, lastName);
        try {
            Student student = studentService.createStudent(firstName, lastName);
            logger.info("<-- Student created successfully: {}", student);
        } catch (IllegalArgumentException e) {
            logger.warn("<-- Failed to create student: {}", e.getMessage());
        }
    }

    /**
     * Handle get student.
     *
     * @param id the id
     */
    public void handleGetStudent(Integer id) {
        logger.info("--> Getting student with ID: {}", id);
        try {
            Student student = studentService.getStudent(id);
            if (student != null) {
                logger.info("<-- Success: {}", student);
            } else {
                logger.warn("<-- Not Found: No student with ID {}", id);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("<-- Invalid request: {}", e.getMessage());
        }
    }

    /**
     * Start the program.
     */
    public void start() {
        logger.info("--- Student_Testing ---");

        this.handleCreateStudent("John", "Doe");
        this.handleCreateStudent("Alice", "");
        this.handleCreateStudent("", "Doe");

        logger.info("");

        this.handleGetStudent(1);
        this.handleGetStudent(2);
        this.handleGetStudent(99);
    }
}