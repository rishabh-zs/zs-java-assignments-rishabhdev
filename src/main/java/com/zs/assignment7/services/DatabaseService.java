package com.zs.assignment7.services;

import com.zs.assignment7.models.Student;
import com.zs.assignment7.repositories.AssignmentRepository;
import java.util.List;

/**
 * The type Database service.
 */
public class DatabaseService {
    private final AssignmentRepository repository = new AssignmentRepository();

    /**
     * Initialize database.
     *
     * @param students the students
     */
    public void initializeDatabase(List<Student> students) {
        repository.callCreateSchema();
        repository.callInsertDepartments();
        repository.callBatchInsertStudents(students);
        repository.callMapStudentsToDepartmentsRandomly();
    }
}