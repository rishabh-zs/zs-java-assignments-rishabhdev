package com.zs.assignment7.services;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.repositories.StudentDepartmentMappingRepository;
import java.util.List;

/**
 * The type Database service.
 */
public class StudentDepartmentMappingService {
    private final StudentDepartmentMappingRepository studentDepartmentMappingRepository;

    public StudentDepartmentMappingService(){
        this.studentDepartmentMappingRepository = new StudentDepartmentMappingRepository();
    }

    /**
     * Initialize database.
     *
     * @param students the students
     */

    public void MapStudentToDepartment(List<Student> students, List<Department> departments) {
        studentDepartmentMappingRepository.createStudentDepartmentMappingSchema();
        studentDepartmentMappingRepository.mapStudentsToDepartmentsRandomly(students, departments);
    }
}
