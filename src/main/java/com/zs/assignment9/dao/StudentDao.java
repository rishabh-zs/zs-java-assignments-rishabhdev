package com.zs.assignment9.dao;

import com.zs.assignment9.model.Student;

/**
 * The interface Student dao.
 */
public interface StudentDao {
    /**
     * Save student.
     *
     * @param student the student
     * @return the student
     */
    Student save(Student student);

    /**
     * Find by id student.
     *
     * @param id the id
     * @return the student
     */
    Student findById(Integer id);
}