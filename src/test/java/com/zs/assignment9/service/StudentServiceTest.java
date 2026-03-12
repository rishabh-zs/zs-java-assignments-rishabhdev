package com.zs.assignment9.service;

import com.zs.assignment9.dao.StudentDao;
import com.zs.assignment9.model.Student;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.params.provider.Arguments;

/**
 * The type Student service test.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    // -------------------------------------------------------------------------
    // createStudent
    // -------------------------------------------------------------------------

    static Stream<Arguments> validCreateStudentInputs() {
        return Stream.of(
                arguments("Jane",  "Doe"),
                arguments("John",  "Smith"),
                arguments("Alice", "Johnson")
        );
    }

    /**
     * Create student valid inputs returns saved student.
     */
    @ParameterizedTest
    @MethodSource("validCreateStudentInputs")
    void createStudent_ValidInputs_ReturnsSavedStudent(String firstName, String lastName) {
        Student mockSavedStudent = new Student(1, firstName, lastName);
        when(studentDao.save(firstName, lastName)).thenReturn(mockSavedStudent);

        Student result = studentService.createStudent(firstName, lastName);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName,  result.getLastName());
        verify(studentDao, times(1)).save(firstName, lastName);
    }

    static Stream<Arguments> invalidCreateStudentInputs() {
        return Stream.of(
                arguments(null,    "Doe",  "First name cannot be empty"),
                arguments("",      "Doe",  "First name cannot be empty"),
                arguments("Alice", null,   "Last name cannot be empty"),
                arguments("Alice", "",     "Last name cannot be empty"),
                arguments(null,    null,   "Both FirstName and LastName cannot be null")
        );
    }

    /**
     * Create student invalid inputs throws exception.
     */
    @ParameterizedTest
    @MethodSource("invalidCreateStudentInputs")
    void createStudent_InvalidInputs_ThrowsException(String firstName, String lastName, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudent(firstName, lastName)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verify(studentDao, never()).save(any(String.class), any(String.class));
    }

    // -------------------------------------------------------------------------
    // getStudent
    // -------------------------------------------------------------------------

    static Stream<Arguments> validGetStudentInputs() {
        return Stream.of(
                arguments(1, "John",  "Doe"),
                arguments(2, "Jane",  "Smith"),
                arguments(3, "Alice", "Johnson")
        );
    }

    /**
     * Get student valid id returns student.
     */
    @ParameterizedTest
    @MethodSource("validGetStudentInputs")
    void getStudent_ValidId_ReturnsStudent(Integer studentId, String firstName, String lastName) {
        Student expectedStudent = new Student(studentId, firstName, lastName);
        when(studentDao.findById(studentId)).thenReturn(expectedStudent);

        Student result = studentService.getStudent(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName,  result.getLastName());
        verify(studentDao, times(1)).findById(studentId);
    }

    /**
     * Get student non-existent id returns null.
     */
    @ParameterizedTest
    @ValueSource(ints = {999, 1000, 500})
    void getStudent_NonExistentId_ReturnsNull(int studentId) {
        when(studentDao.findById(studentId)).thenReturn(null);

        Student result = studentService.getStudent(studentId);

        assertNull(result);
        verify(studentDao, times(1)).findById(studentId);
    }

    static Stream<Arguments> invalidStudentIds() {
        return Stream.of(
                arguments((Object) null),
                arguments(0),
                arguments(-1)
        );
    }

    /**
     * Get student invalid id throws exception.
     */
    @ParameterizedTest
    @MethodSource("invalidStudentIds")
    void getStudent_InvalidId_ThrowsException(Integer id) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.getStudent(id)
        );

        assertEquals("Invalid Student ID", exception.getMessage());
        verify(studentDao, never()).findById(anyInt());
    }
}
