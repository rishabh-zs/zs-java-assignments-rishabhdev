package com.zs.assignment9.controller;

import com.zs.assignment9.model.Student;
import com.zs.assignment9.service.StudentService;

/**
 * The type Student controller.
 */
public class StudentController {

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
        try {
            System.out.println("--> Request: Create student '" + firstName + " " + lastName + "'");
            Student student = studentService.createStudent(firstName, lastName);
            System.out.println("<-- Success: " + student);
        } catch (IllegalArgumentException e) {
            System.out.println("<-- Error: " + e.getMessage());
        }
    }

    /**
     * Handle get student.
     *
     * @param id the id
     */
    public void handleGetStudent(Integer id) {
        try {
            System.out.println("--> Request: Fetch student ID " + id);
            Student student = studentService.getStudent(id);
            if (student != null) {
                System.out.println("<-- Success: " + student);
            } else {
                System.out.println("<-- Not Found: No student with ID " + id);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("<-- Error: " + e.getMessage());
        }
    }

    /**
     * Start the program.
     */
    public void start() {
        System.out.println("--- Student_Testing---\n");

        this.handleCreateStudent("John", "Doe");
        this.handleCreateStudent("Alice", "");
        this.handleCreateStudent("", "Doe");

        System.out.println();

        this.handleGetStudent(1);
        this.handleGetStudent(2);
        this.handleGetStudent(99);
    }
}