package com.zs.assignment9.model;

/**
 * The type Student.
 */
public class Student {
    private Integer id;
    private String firstName;
    private String lastName;

    /**
     * Instantiates a new Student.
     *
     * @param id        the id
     * @param firstName the first name
     * @param lastName  the last name
     */
    public Student(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName + "'}";
    }
}