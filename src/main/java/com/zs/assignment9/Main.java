package com.zs.assignment9;

import com.zs.assignment9.controller.StudentController;
import com.zs.assignment9.service.StudentService;
import com.zs.assignment9.dao.StudentDaoClass;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        StudentService studentService = new StudentService(new StudentDaoClass());
        StudentController studentController = new StudentController(studentService);
        studentController.start();
    }
}