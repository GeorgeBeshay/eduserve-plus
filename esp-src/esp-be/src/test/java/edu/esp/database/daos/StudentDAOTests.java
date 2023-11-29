package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.database.doas.StudentDAO;
import edu.esp.system_entities.system_users.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EspBeApplication.class)
public class StudentDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StudentDAO studentDAO;
    private final Random random = new Random();

    @BeforeEach
    public void setUp(){
        this.studentDAO = new StudentDAO(jdbcTemplate);
    }

    @AfterAll
    public static void closingStatement(){
        System.out.println("\u001B[32m" + "All Student DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Student DAO - Valid Insertion Test Case")
    public void testCreateStudent_Success(){
        int randomNumber = random.nextInt(100);

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(randomNumber);
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn(Integer.toString(randomNumber));

        assertTrue(this.studentDAO.createStudent(newStudent));

        // delete the inserted student record.
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateStudent_Failure_invalid_id(){
        int randomNumber = random.nextInt(100);

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(-1);     // setting invalid id
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn(Integer.toString(randomNumber));

        assertFalse(this.studentDAO.createStudent(newStudent));

        // delete the inserted student record.
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateStudent_Failure_duplicate_id(){
        int randomNumber = random.nextInt(100);

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(randomNumber);     // setting invalid id
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn(Integer.toString(randomNumber));

        assertTrue(this.studentDAO.createStudent(newStudent));
        assertFalse(this.studentDAO.createStudent(newStudent));     // duplicate record

        // delete the inserted student record.
    }

    @Test
    @DisplayName("Student DAO - Read record of ID = 1")
    public void testReadStudentById1() {
        Student student = this.studentDAO.readStudentById(1);

        assertNotNull(student);
        assertEquals(1, student.getStudentId());
    }

    @Test
    @DisplayName("Student DAO - Read invalid record of ID = -1")
    public void testReadStudentById2() {
        Student student = this.studentDAO.readStudentById(-1);

        assertNull(student);
    }
    @Test
    @DisplayName("Student DAO - select all students")
    public void testSelectAll(){
        List<Student> students = this.studentDAO.SelectAll();

        assertNotEquals(null,students);
    }

    @Test
    @DisplayName("Student DAO - delete valid student with ID = 3")
    public void deleteStudentById1() {
        assertTrue(this.studentDAO.deleteStudentById(3));
    }

    @Test
    @DisplayName("Student DAO - delete invalid student with ID = 19")
    public void deleteStudentById2() {
        assertFalse(this.studentDAO.deleteStudentById(19));
    }

    @Test
    @DisplayName("Student DAO - delete valid student with ID = 2, then try to delete the same student again")
    public void deleteStudentById3() {
        assertTrue(this.studentDAO.deleteStudentById(2));
        assertFalse(this.studentDAO.deleteStudentById(2));
    }


    @Test
    @DisplayName("Student DAO - Sign up a student by moving from unregistered students to complete students table")
    public void signUpStudent1() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(1545);
        registeredStudent.setStudentName("Signed Up Student");
        registeredStudent.setDepartmentId((byte) 3);
        assertTrue(this.studentDAO.signUpStudent(1545, 989, registeredStudent));
    }

    @Test
    @DisplayName("Student DAO - Sign up a student whose id is not in the unregistered table")
    public void signUpStudent2() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(155);
        registeredStudent.setStudentName("Unregistered Student");
        registeredStudent.setDepartmentId((byte) 2);
        assertFalse(this.studentDAO.signUpStudent(155, 754, registeredStudent));
    }

    @Test
    @DisplayName("Student DAO - Sign up a student with a valid id but invalid temporary password")
    public void signUpStudent3() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(123456);
        registeredStudent.setStudentName("Fake Student");
        registeredStudent.setDepartmentId((byte) 1);
        assertFalse(this.studentDAO.signUpStudent(123456, 414, registeredStudent));
    }

}
