package com.zs.assignment7.services;

import com.zs.assignment7.models.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data generation service.
 */
public class StudentService {
    public List<Student> generateStudents(int count) {
        System.out.println("------Generating " + count + " student records in memory------");
        List<Student> students = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            String firstName = "First" + i;
            String lastName = "Last" + i;
            String mobile = "9" + String.format("%09d", i % 1000000000);
            students.add(new Student(i, firstName, lastName, mobile));
        }
        return students;
    }
}