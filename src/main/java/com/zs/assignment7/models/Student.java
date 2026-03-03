package com.zs.assignment7.models;

/**
 * The type Student.
 */
public class Student {
    private final String firstName;
    private final String lastName;
    private final String mobile;

    /**
     * Instantiates a new Student.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param mobile    the mobile
     */
    public Student(String firstName, String lastName, String mobile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
    }

    /**
     * Gets mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
}
