package com.zs.assignment7.controllers;

import com.zs.assignment7.services.DepartmentService;
import com.zs.assignment7.models.Department;

import java.util.List;

/**
 * The type Department controller.
 */
public class DepartmentController {
    /**
     * The Department service.
     */
    DepartmentService departmentService;

    /**
     * Instantiates a new Department controller.
     */
    public DepartmentController(){
        this.departmentService=new DepartmentService();
    }

    /**
     * Execute department flow list.
     *
     * @return the list
     */
    public List<Department> executeDepartmentFlow() {
        departmentService.createDepartmentSchema();
        return departmentService.insertDepartment();
    }
}
