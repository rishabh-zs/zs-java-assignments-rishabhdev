package com.zs.assignment7;

import com.zs.assignment7.controllers.FileExportController;
import com.zs.assignment7.controllers.DepartmentController;
import com.zs.assignment7.controllers.StudentController;
import com.zs.assignment7.controllers.StudentDepartmentMappingController;
import com.zs.assignment7.models.Department;
import com.zs.assignment7.models.Student;

import java.util.List;


/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        StudentController studentController = new StudentController();
        DepartmentController departmentController = new DepartmentController();
        StudentDepartmentMappingController studentDepartmentMappingController = new StudentDepartmentMappingController();
        FileExportController fileExportController = new FileExportController();

        List<Student> students = studentController.executeStudentFlow();
        List<Department> departments = departmentController.executeDepartmentFlow();
        studentDepartmentMappingController.executeStudentDepartmentMappingFlow(students, departments);
        fileExportController.executeFileExportFlow("student_departments.csv.gz");

    }
}
