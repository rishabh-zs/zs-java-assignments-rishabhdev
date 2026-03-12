package com.zs.assignment7.services;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.repositories.DepartmentRepository;

import java.util.List;

/**
 * The type Department service.
 */
public class DepartmentService {
    /**
     * The Department repository.
     */
    DepartmentRepository departmentRepository;

    /**
     * Instantiates a new Department service.
     */
    public DepartmentService(){
        this.departmentRepository=new DepartmentRepository();
    }


    /**
     * Create department schema.
     */
    public void createDepartmentSchema(){
        try {
            departmentRepository.createDepartmentSchema();
        } catch (Exception e) {
            System.err.println("Error creating department schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Insert department list.
     *
     * @return the list
     */
    public List<Department> insertDepartment(){
        List<Department> departments = List.of(
                new Department(1, "CS"),
                new Department(2, "EE"),
                new Department(3, "Mech")
        );

        try {
            departmentRepository.insertDepartments(departments);
        } catch (Exception e) {
            System.err.println("Error inserting departments: " + e.getMessage());
            e.printStackTrace();
        }
        return departments;
    }
}
