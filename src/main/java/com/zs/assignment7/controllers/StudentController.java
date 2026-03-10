package com.zs.assignment7.controllers;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.services.StudentService;

import java.util.List;

/**
 * The type Student controller.
 */
public class StudentController {
    private final int count=1000000;
    private final StudentService studentService;

    /**
     * Instantiates a new Student controller.
     */
    public StudentController(){
        this.studentService=new StudentService();
    }

    /**
     * Execute student flow list.
     *
     * @return the list
     */
    public List<Student> executeStudentFlow() {
        List<Student> students = studentService.generateStudents(count);
        studentService.createStudentSchema();
        studentService.InsertStudents();
        return students;
    }
}
