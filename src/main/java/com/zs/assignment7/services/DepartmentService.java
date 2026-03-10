package com.zs.assignment7.services;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.repositories.DepartmentRepository;

import java.util.List;

public class DepartmentService {
    DepartmentRepository departmentRepository;

    public DepartmentService(){
        this.departmentRepository=new DepartmentRepository();
    }


    public void createDepartmentSchema(){
        try {
            departmentRepository.createDepartmentSchema();
        } catch (Exception e) {
            System.err.println("Error creating department schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
