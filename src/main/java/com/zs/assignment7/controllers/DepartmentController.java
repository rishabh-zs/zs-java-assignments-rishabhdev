package com.zs.assignment7.controllers;

import com.zs.assignment7.services.DepartmentService;
import com.zs.assignment7.models.Department;

import java.util.List;

public class DepartmentController {
    DepartmentService departmentService;

    public DepartmentController(){
        this.departmentService=new DepartmentService();
    }

    public List<Department> executeDepartmentFlow() {
        departmentService.createDepartmentSchema();
        return departmentService.insertDepartment();
    }
}
