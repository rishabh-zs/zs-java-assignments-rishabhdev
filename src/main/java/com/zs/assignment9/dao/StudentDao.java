package com.zs.assignment9.dao;

import com.zs.assignment9.model.Student;

/**
 * The interface Student dao.
 */
public interface StudentDao {
    /**
     * Save student.
     *
     * @param firstname the firstname
     * @param lastname  the lastname
     * @return the student
     */
    Student save(String firstname,String lastname);

    /**
     * Find by id student.
     *
     * @param id the id
     * @return the student
     */
    Student findById(Integer id);
}