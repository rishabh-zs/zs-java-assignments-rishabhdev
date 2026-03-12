package com.zs.assignment9.service;

import com.zs.assignment9.dao.StudentDao;
import com.zs.assignment9.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * The type Student service test.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    /**
     * Create student valid inputs returns saved student.
     */
    @Test
    void createStudent_ValidInputs_ReturnsSavedStudent() {
        String firstName = "Jane";
        String lastName = "Doe";
        Student mockSavedStudent = new Student(1, firstName, lastName);

        when(studentDao.save(, any(Student.class), )).thenReturn(mockSavedStudent);

        Student result = studentService.createStudent(firstName, lastName);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentDao, times(1)).save(, any(Student.class), );
    }

    /**
     * Create student null first name throws exception.
     */
    @Test
    void createStudent_NullFirstName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudent(null, "Doe")
        );

        assertEquals("First name cannot be empty", exception.getMessage());
        verify(studentDao, never()).save(, any(Student.class), );
    }

    /**
     * Create student null last name throws exception.
     */
    @Test
    void createStudent_NullLastName_ThrowsException() {
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,
                ()->studentService.createStudent("Alice",null)
        );

        assertEquals("Last name cannot be empty", exception.getMessage());
        verify(studentDao, never()).save(, any(Student.class), );
    }

    /**
     * Create student both names null throws exception.
     */
    @Test
    void createStudent_BothNamesNull_ThrowsException() {
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,
                ()->studentService.createStudent(null,null)
        );

        assertEquals("Both FirstName and LastName cannot be null", exception.getMessage());
        verify(studentDao, never()).save(, any(Student.class), );
    }


    /**
     * Gets student valid id returns student.
     */
    @Test
    void getStudent_ValidId_ReturnsStudent() {
        // Arrange
        Integer studentId = 1;
        Student expectedStudent = new Student(studentId, "John", "Doe");

        when(studentDao.findById(studentId)).thenReturn(expectedStudent);

        // Act
        Student result = studentService.getStudent(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("John", result.getFirstName());
        verify(studentDao, times(1)).findById(studentId);
    }

    /**
     * Gets student in valid id returns null.
     */
    @Test
    void getStudent_InValidId_ReturnsNull() {
        Integer studentId = 999;
        when(studentDao.findById(studentId)).thenReturn(null);

        Student result = studentService.getStudent(studentId);

        assertNull(result);
        verify(studentDao, times(1)).findById(studentId);
    }

    /**
     * Gets student null id throws exception.
     */
    @Test
    void getStudent_NullId_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.getStudent(null)
        );

        assertEquals("Invalid Student ID", exception.getMessage());
        verify(studentDao, never()).findById(anyInt());
    }
}
