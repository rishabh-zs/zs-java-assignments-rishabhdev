package com.zs.assignment9.service;

import com.zs.assignment9.dao.StudentDao;
import com.zs.assignment9.model.Student;

/**
 * The type Student service.
 */
public class StudentService {

    private final StudentDao studentDao;
    private int ID=0;

    /**
     * Instantiates a new Student service.
     *
     * @param studentDao the student dao
     */
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    /**
     * Create student.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return the student
     */
    public Student createStudent(String firstName, String lastName) {
        if(firstName==null && lastName==null){
            throw new IllegalArgumentException("Both FirstName and LastName cannot be null");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        ID=ID+1;

        Student newStudent = new Student(ID, firstName, lastName);
        return studentDao.save(newStudent);
    }

    /**
     * Gets student.
     *
     * @param id the id
     * @return the student
     */
    public Student getStudent(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Student ID");
        }
        return studentDao.findById(id);
    }
}