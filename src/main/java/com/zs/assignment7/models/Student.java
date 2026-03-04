package com.zs.assignment7.models;

/**
 * The type Student.
 */
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String mobile;

    /**
     * Instantiates a new Student.
     *
     * @param id        the id
     * @param firstName the first name
     * @param lastName  the last name
     * @param mobile    the mobile
     */
    public Student(int id,String firstName, String lastName, String mobile) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
    }

    /**
     * Get id int.
     *
     * @return the int
     */
    public int getId(){
        return id;
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
