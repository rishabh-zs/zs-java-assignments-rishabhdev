package com.zs.assignment7.controllers;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.services.StudentDepartmentMappingService;

import java.util.List;

public class StudentDepartmentMappingController {
    StudentDepartmentMappingService studentDepartmentMappingService;

    public StudentDepartmentMappingController(){
        this.studentDepartmentMappingService = new StudentDepartmentMappingService();
    }

    public void executeStudentDepartmentMappingFlow(List<Student> students, List<Department> departments) {
        studentDepartmentMappingService.MapStudentToDepartment(students, departments);
    }
}
