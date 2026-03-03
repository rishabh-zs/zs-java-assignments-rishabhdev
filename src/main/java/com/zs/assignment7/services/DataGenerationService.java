package com.zs.assignment7.services;

import com.zs.assignment7.models.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data generation service.
 */
public class DataGenerationService {
    /**
     * Generate students list.
     *
     * @param count the count
     * @return the list
     */
    public List<Student> generateStudents(int count) {
        System.out.println("Generating " + count + " student records in memory...");

        List<Student> students = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            students.add(new Student(
                    "FirstName" + i,
                    "LastName" + i,
                    String.format("9%09d", i % 1000000000)
            ));
        }

        System.out.println("Data generation complete.");
        return students;
    }
}