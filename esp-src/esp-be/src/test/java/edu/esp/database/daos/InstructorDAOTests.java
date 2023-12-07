package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Instructor;
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
    @DisplayName("Instructor DAO - Invalid Insertion Test Case with duplicate id")
    public void testCreateInstructor_Failure_duplicate_id(){

        Instructor newInstructor = new Instructor();
        newInstructor.setInstructorId((byte) 1);     // setting duplicate id
        newInstructor.setDepartmentId((byte) 1);

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Read record of ID = 1")
    public void testReadInstructorById1() {
        Instructor instructor = this.instructorDAO.readInstructorById(1);

        assertNotNull(instructor);
        assertEquals(1, instructor.getInstructorId());
    }

    @Test
    @DisplayName("Instructor DAO - Read invalid record of ID = -1")
    public void testReadInstructorById2() {
        Instructor instructor = this.instructorDAO.readInstructorById(-1);

        assertNull(instructor);
    }
    @Test
    @DisplayName("Instructor DAO - Select all instructors")
    public void testSelectAll(){
        List<Instructor> instructors = this.instructorDAO.SelectAll();

        assertNotEquals(null,instructors);
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 2")
    public void deleteInstructorById1() {
        assertTrue(this.instructorDAO.deleteInstructorById(2));
    }

    @Test
    @DisplayName("Instructor DAO - delete invalid instructor with ID = 10")
    public void deleteInstructorById2() {
        assertFalse(this.instructorDAO.deleteInstructorById(10));
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 3, then try to delete the same instructor again")
    public void deleteInstructorById3() {
        assertTrue(this.instructorDAO.deleteInstructorById(3));
        assertFalse(this.instructorDAO.deleteInstructorById(3));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor by moving from unregistered instructors to complete instructors table")
    public void signUpInstructor1() {
        Instructor registeredInstructor = new Instructor(); // Create an instructor object with test data
        registeredInstructor.setInstructorId((byte) 15);
        registeredInstructor.setInstructorName("Signed Up Instructor");
        registeredInstructor.setDepartmentId((byte) 2);
        assertTrue(this.instructorDAO.signUpInstructor((byte)15, 999, registeredInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor whose id is not in the unregistered table")
    public void signUpInstructor2() {
        Instructor registeredInstructor = new Instructor(); // Create an instructor object with test data
        registeredInstructor.setInstructorId((byte) 14);
        registeredInstructor.setInstructorName("Unregistered Instructor");
        registeredInstructor.setDepartmentId((byte) 2);
        assertFalse(this.instructorDAO.signUpInstructor((byte)14, 999, registeredInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor with a valid id but invalid temporary password")
    public void signUpInstructor3() {
        Instructor registeredInstructor = new Instructor(); // Create an instructor object with test data
        registeredInstructor.setInstructorId((byte) 16);
        registeredInstructor.setInstructorName("Fake Instructor");
        registeredInstructor.setDepartmentId((byte) 3);
        assertFalse(this.instructorDAO.signUpInstructor((byte)16, 404, registeredInstructor));
    }
}
