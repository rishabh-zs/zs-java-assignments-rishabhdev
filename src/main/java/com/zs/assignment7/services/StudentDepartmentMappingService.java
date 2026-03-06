package com.zs.assignment7.services;

import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;
import com.zs.assignment7.repositories.StudentDepartmentRepository;
import java.util.List;

/**
 * The type Database service.
 */
public class StudentDepartmentMappingService {
    private final StudentDepartmentRepository repository = new StudentDepartmentRepository();

    /**
     * Initialize database.
     *
     * @param students the students
     */
    public void initializeDatabase(List<Student> students) {
        List<Department> departments = List.of(
                new Department(1, "CS"),
                new Department(2, "EE"),
                new Department(3, "Mech")
        );
        repository.createSchema();
        repository.insertDepartments(departments);
        repository.batchInsertStudents(students);
        repository.mapStudentsToDepartmentsRandomly(students, departments);
    }
}
