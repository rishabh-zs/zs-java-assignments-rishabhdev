package com.zs.assignment9.dao;

import com.zs.assignment9.model.Student;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Student dao class.
 */
public class StudentDaoClass implements StudentDao {

    private final Map<Integer, Student> database = new HashMap<>();

    @Override
    public Student save(Student student) {
        Student savedStudent = new Student(student.getId(), student.getFirstName(), student.getLastName());

        database.put(savedStudent.getId(), savedStudent);
        return savedStudent;
    }

    @Override
    public Student findById(Integer id) {
        return database.get(id);
    }
}