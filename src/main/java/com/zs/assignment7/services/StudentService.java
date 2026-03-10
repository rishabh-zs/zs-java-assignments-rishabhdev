package com.zs.assignment7.services;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.repositories.StudentRepository;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Student service.
 */
public class StudentService {
    private List<Student> list=new ArrayList<>();
    StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository=new StudentRepository();
    }
    /**
     * Generate students list.
     *
     * @param count the count
     * @return the list
     */
    public List<Student> generateStudents(int count) {
        System.out.println("------Generating " + count + " student records in memory------");
        List<Student> students = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            String firstName = "First" + i;
            String lastName = "Last" + i;
            String mobile = "9" + String.format("%09d", i % 1000000000);
            students.add(new Student(i, firstName, lastName, mobile));
        }
        list=students;
        return students;
    }

    public void createStudentSchema(){
        try {
            studentRepository.createStudentSchema();
        } catch (Exception e) {
            System.err.println("Error creating student schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void InsertStudents() {
        try {
            studentRepository.batchInsertStudents(list);
        } catch (Exception e) {
            System.err.println("Error inserting students: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
