package com.zs.assignment7.models;

/**
 * The type Department.
 */
public class Department {
    private int id;
    private String deptName;

    /**
     * Instantiates a new Department.
     *
     * @param id       the id
     * @param deptName the dept name
     */
    public Department(int id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets dept name.
     *
     * @return the dept name
     */
    public String getDeptName() {
        return deptName;
    }
}