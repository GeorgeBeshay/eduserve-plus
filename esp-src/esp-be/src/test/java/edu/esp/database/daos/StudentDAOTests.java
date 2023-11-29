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

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
