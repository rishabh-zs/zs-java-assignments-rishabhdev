package com.zs.assignment7.models;

/**
 * The type Department.
 */
public class Department {
    private int id;
    private String name;

    /**
     * Instantiates a new Department.
     *
     * @param id   the id
     * @param name the name
     */
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}