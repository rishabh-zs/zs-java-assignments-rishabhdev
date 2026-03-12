package com.zs.assignment7.models;

/**
 * The type Student department mapping.
 */
public class StudentDepartmentMapping {
    private int student_id;
    private int dept_id;

    /**
     * Instantiates a new Student department mapping.
     *
     * @param student_id the student id
     * @param dept_id    the dept id
     */
    public StudentDepartmentMapping(int student_id, int dept_id) {
        this.student_id = student_id;
        this.dept_id = dept_id;
    }

    /**
     * Gets dept id.
     *
     * @return the dept id
     */
    public int getDept_id() {
        return dept_id;
    }

    /**
     * Sets dept id.
     *
     * @param dept_id the dept id
     */
    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    /**
     * Gets student id.
     *
     * @return the student id
     */
    public int getStudent_id() {
        return student_id;
    }

    /**
     * Sets student id.
     *
     * @param student_id the student id
     */
    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}


