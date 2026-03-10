package com.zs.assignment7.services;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.repositories.StudentDepartmentMappingRepository;
import java.util.List;

/**
 * The type Student department mapping service.
 */
public class StudentDepartmentMappingService {
    private final StudentDepartmentMappingRepository studentDepartmentMappingRepository;

    /**
     * Instantiates a new Student department mapping service.
     */
    public StudentDepartmentMappingService(){
        this.studentDepartmentMappingRepository = new StudentDepartmentMappingRepository();
    }

    /**
     * Map student to department.
     *
     * @param students    the students
     * @param departments the departments
     */
    public void MapStudentToDepartment(List<Student> students, List<Department> departments) {
        studentDepartmentMappingRepository.createStudentDepartmentMappingSchema();
        studentDepartmentMappingRepository.mapStudentsToDepartmentsRandomly(students, departments);
    }
}
