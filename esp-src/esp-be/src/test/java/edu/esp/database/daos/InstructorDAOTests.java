package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.database.doas.InstructorDAO;
import edu.esp.system_entities.system_users.Instructor;
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
public class InstructorDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private InstructorDAO instructorDAO;
    private final Random random = new Random();

    @BeforeEach
    public void setUp(){
        this.instructorDAO = new InstructorDAO(jdbcTemplate);
    }

    @AfterAll
    public static void closingStatement(){
        System.out.println("\u001B[32m" + "All Instructor DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Instructor DAO - Valid Insertion Test Case")
    public void testCreateInstructor_Success(){
        int randomNumber = random.nextInt(100) + 2;

        Instructor newInstructor = new Instructor(); // Create a Student object with test data
        newInstructor.setInstructorId((byte) randomNumber);
        newInstructor.setDepartmentId((byte) 1);

        assertTrue(this.instructorDAO.createInstructor(newInstructor));

        // delete the inserted record.
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateInstructor_Failure_invalid_id(){
        Instructor newInstructor = new Instructor(); // Create a Student object with test data

        newInstructor.setInstructorId((byte) -1);
        newInstructor.setDepartmentId((byte) 1);

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateInstructor_Failure_duplicate_id(){

        Instructor newInstructor = new Instructor();
        newInstructor.setInstructorId((byte) 1);     // setting duplicate id
        newInstructor.setDepartmentId((byte) 1);

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

}
