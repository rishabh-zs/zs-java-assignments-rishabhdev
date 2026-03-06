package com.zs.assignment9;

import com.zs.assignment9.controller.StudentController;

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
        StudentController studentController = new StudentController();
        studentController.start();
    }
}