package com.zs.assignment9.service;

import com.zs.assignment9.dao.StudentDao;
import com.zs.assignment9.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Student service.
 */
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentDao studentDao;

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
        logger.debug("--> createStudent called: firstName='{}', lastName='{}'", firstName, lastName);
        if (firstName == null && lastName == null) {
            logger.warn("Validation failed: both firstName and lastName are null");
            throw new IllegalArgumentException("Both FirstName and LastName cannot be null");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            logger.warn("Validation failed: firstName is empty or null");
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            logger.warn("Validation failed: lastName is empty or null");
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        try {
            Student student = studentDao.save(firstName, lastName);
            logger.debug("<-- Student saved: {}", student);
            return student;
        } catch (Exception ex) {
            logger.error("Error saving student: firstName='{}', lastName='{}'", firstName, lastName, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets student.
     *
     * @param id the id
     * @return the student
     */
    public Student getStudent(Integer id) {
        logger.debug("--> getStudent called: id={}", id);
        if (id == null || id <= 0) {
            logger.warn("Validation failed: invalid student ID={}", id);
            throw new IllegalArgumentException("Invalid Student ID");
        }
        try {
            Student student = studentDao.findById(id);
            if (student != null) {
                logger.debug("<-- Student found: {}", student);
            } else {
                logger.debug("<-- No student found with ID={}", id);
            }
            return student;
        } catch (Exception ex) {
            logger.error("Error retrieving student with ID={}", id, ex);
            throw new RuntimeException(ex);
        }
    }
}