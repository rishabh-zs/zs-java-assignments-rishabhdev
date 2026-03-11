package com.zs.assignment7.controllers;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.services.StudentDepartmentMappingService;

import java.util.List;

/**
 * The type Student department mapping controller.
 */
public class StudentDepartmentMappingController {
    /**
     * The Student department mapping service.
     */
    StudentDepartmentMappingService studentDepartmentMappingService;

    /**
     * Instantiates a new Student department mapping controller.
     */
    public StudentDepartmentMappingController(){
        this.studentDepartmentMappingService = new StudentDepartmentMappingService();
    }

    /**
     * Execute student department mapping flow.
     *
     * @param students    the students
     * @param departments the departments
     */
    public void executeStudentDepartmentMappingFlow(List<Student> students, List<Department> departments) {
        studentDepartmentMappingService.MapStudentToDepartment(students, departments);
    }
}
