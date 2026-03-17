package com.zs.assignment9.dao;

import com.zs.assignment9.model.Student;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Student dao class.
 */
public class StudentDaoClass implements StudentDao {

    private int ID=0;
    private final Map<Integer, Student> database = new HashMap<>();

    @Override
    public Student save(String firstName, String lastName) {
        ID=ID+1;
        Student savedStudent = new Student(ID,firstName,lastName);

        database.put(savedStudent.getId(), savedStudent);
        return savedStudent;
    }

    @Override
    public Student findById(Integer id) {
        return database.get(id);
    }
}