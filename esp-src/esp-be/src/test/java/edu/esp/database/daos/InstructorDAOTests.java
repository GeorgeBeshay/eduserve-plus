package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class InstructorDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private InstructorDAO instructorDAO;
    private final Random random = new Random();

    @BeforeAll
    public void setUp() {
        jdbcTemplate.update("""
                INSERT INTO department (dpt_id, dpt_name)
                VALUES
                  (101, 'Test DPT 1'),
                  (102, 'Test DPT 2'),
                  (103, 'Test DPT 3');
                """);
        this.instructorDAO = new InstructorDAO(jdbcTemplate);
    }

    @AfterAll
    public void closingStatement() {
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id IN (101,102,103);");
        System.out.println("\u001B[32m" + "All Instructor DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Instructor DAO - Valid Insertion Test Case")
    public void testCreateInstructorSuccess() {
        // Create an instructor object with test data
        int insertedInstructorId = random.nextInt(1, 100);
        Instructor newInstructor = new Instructor(
            insertedInstructorId, 354, (byte) 101,
            "Test Instructor", null, "instructor1@uni.com", null
        );

        assertTrue(this.instructorDAO.createInstructor(newInstructor));

        // Delete inserted instructor
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = %d;".formatted(insertedInstructorId));
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case with id = -1")
    public void testCreateInstructorFailureInvalidId() {
        // Create an instructor object with ID = -1
        Instructor newInstructor = new Instructor(
            -1, 504, (byte) 101,
            "Invalid ID Instructor", null, "instructor2@uni.com", null
        );

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case with duplicate id")
    public void testCreateInstructorFailureDuplicateId() {
        // Create an instructor object with ID = -1
        Instructor newInstructor1 = new Instructor(
                3, 43055, (byte) 102,
            "New Instructor", null, "instructor3@uni.com", null
        );
        // Create an instructor object with ID = -1
        Instructor newInstructor2 = new Instructor(
            3, 8928, (byte) 103,
            "Duplicate ID Instructor", null, "instructor4@uni.com", null
        );

        assertTrue(this.instructorDAO.createInstructor(newInstructor1)); // Valid instructor
        assertFalse(this.instructorDAO.createInstructor(newInstructor2)); // Instructor with duplicate ID

        // Delete inserted instructor
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = 3;");
    }

    @Test
    @DisplayName("Instructor DAO - Read record of ID = 1")
    public void testReadInstructorByValidId() {
        // Insert a test instructor object
        jdbcTemplate.batchUpdate("""
                INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
                VALUES (1, 135790, 101, 'Prof. Anderson', '555-4321', 'prof.anderson@example.com', 'Monday 10am-12pm, Wednesday 2pm-4pm');
                """);

        Instructor instructor = this.instructorDAO.readInstructorById(1);

        assertEquals(1, instructor.getInstructorId());
        assertEquals(135790, instructor.getInstructorPwHash());
        assertEquals(101, instructor.getDptId());
        assertEquals("Prof. Anderson", instructor.getInstructorName());
        assertEquals("555-4321", instructor.getPhone());
        assertEquals("prof.anderson@example.com", instructor.getEmail());
        assertEquals("Monday 10am-12pm, Wednesday 2pm-4pm", instructor.getOfficeHrs());

        // Delete inserted instructor
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = 1;");
    }

    @Test
    @DisplayName("Instructor DAO - Read invalid record of ID = -1")
    public void testReadInstructorByInvalidId() {
        Instructor instructor = this.instructorDAO.readInstructorById(-1);

        assertNull(instructor);
    }

    @Test
    @DisplayName("Instructor DAO - create an instructor object then read it with DAO methods")
    public void testCreateThenReadInstructor() {
        // Create an instructor object with test data
        int insertedInstructorId = random.nextInt(1, 100);
        int pwHash = random.nextInt(-10000, 10000);
        Instructor newInstructor = new Instructor(
                insertedInstructorId, pwHash, (byte) 101,
                "Test Instructor", "555-4321", "instructor1@uni.com", "Monday 10am-12pm, Wednesday 2pm-4pm"
        );

        assertTrue(this.instructorDAO.createInstructor(newInstructor));

        Instructor instructor = this.instructorDAO.readInstructorById(insertedInstructorId);

        assertEquals(insertedInstructorId, instructor.getInstructorId());
        assertEquals(pwHash, instructor.getInstructorPwHash());
        assertEquals(101, instructor.getDptId());
        assertEquals("Test Instructor", instructor.getInstructorName());
        assertEquals("555-4321", instructor.getPhone());
        assertEquals("instructor1@uni.com", instructor.getEmail());
        assertEquals("Monday 10am-12pm, Wednesday 2pm-4pm", instructor.getOfficeHrs());

        // Delete inserted instructor
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = %d;".formatted(insertedInstructorId));
    }

    @Test
    @DisplayName("Instructor DAO - Select all instructors")
    public void testSelectAll() {
        // Insert a list of instructors
        jdbcTemplate.batchUpdate("""
                INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
                VALUES
                  (1, 135790, 101, 'Prof. Anderson', '555-4321', 'prof.anderson@example.com', 'Monday 10am-12pm, Wednesday 2pm-4pm'),
                  (2, 246801, 102, 'Dr. Thompson', '555-6789', 'dr.thompson@example.com', 'Tuesday 9am-11am, Thursday 1pm-3pm'),
                  (3, 987123, 103, 'Prof. Williams', '555-9876', 'prof.williams@example.com', 'Friday 3pm-5pm, Saturday 10am-12pm');
                """);

        List<Instructor> instructors = this.instructorDAO.SelectAll();

        assertNotNull(instructors);
        assertTrue(instructors.size() >= 3); // Size is at least 3

        // Check that the list contains the inserted IDs
        boolean[] containsId = {false,false,false};
        int id;
        for (Instructor instructor : instructors) {
            id = instructor.getInstructorId();
            if (id == 1 || id == 2 || id == 3)
                containsId[id-1] = true;
        }
        assertArrayEquals(new boolean[]{true,true,true}, containsId);

        // Delete the instructor list
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id IN (1,2,3);");
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 2")
    public void testDeleteInstructorByValidId() {
        // Insert an instructor with ID = 2
        jdbcTemplate.update("""
                INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
                VALUES (2, 246801, 102, 'Dr. Thompson', '555-6789', 'dr.thompson@example.com', 'Tuesday 9am-11am, Thursday 1pm-3pm');
                """);

        assertTrue(this.instructorDAO.deleteInstructorById(2));
    }

    @Test
    @DisplayName("Instructor DAO - delete invalid instructor with ID = 10")
    public void testDeleteInstructorByInvalidId() {
        assertFalse(this.instructorDAO.deleteInstructorById(10));
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 3, then try to delete the same instructor again")
    public void testDeleteInstructorTwice() {
        // Insert an instructor with ID = 3
        jdbcTemplate.update("""
                INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
                VALUES (3, 987123, 103, 'Prof. Williams', '555-9876', 'prof.williams@example.com', 'Friday 3pm-5pm, Saturday 10am-12pm');
                """);

        assertTrue(this.instructorDAO.deleteInstructorById(3));
        assertFalse(this.instructorDAO.deleteInstructorById(3));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor by moving from unregistered instructors to complete instructors table")
    public void testSignUpValidInstructor() {
        // Insert an unregistered instructor with ID = 4
        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES
                    (4, 999, 102);
                """);

        // Create an instructor object with same ID and test data
        Instructor registeredInstructor = new Instructor(
            4, 9847, (byte)-1,
            "Signed Up Instructor", null, "instructor5@uni.com", null
        );

        assertTrue(this.instructorDAO.signUpInstructor(4, 999, registeredInstructor));
        // Check that the instructor does not exist anymore in the unregistered instructors table
        assertNull(this.instructorDAO.readUnregisteredInstructorById(4));
        // Read the registered instructor and make sure the department ID
        // is the same as the one that was in the unregistered table
        assertEquals(102, this.instructorDAO.readInstructorById(4).getDptId());

        // Delete the instructor used in the test
        jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = 4;");
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor whose id is not in the unregistered table")
    public void testSignUpInvalidInstructor() {
        // Create an instructor object with test data
        Instructor registeredInstructor = new Instructor(
                14, 1001, (byte) 102,
                "Unregistered Instructor", null, "instructor6@uni.com", null
        );

        assertFalse(this.instructorDAO.signUpInstructor(14, 987, registeredInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor with a valid id but invalid temporary password")
    public void testSignUpInstructorInvalidPassword() {
        // Insert an unregistered instructor with ID = 5
        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES
                    (5, 120, 103);
                """);


        // Create an instructor object with same ID and test data, but sign up with a wrong OTP
        Instructor registeredInstructor = new Instructor(
                5, 444, (byte) -1,
                "Fake Instructor", null, "instructorfake@uni.com", null
        );

        assertFalse(this.instructorDAO.signUpInstructor(5, 404, registeredInstructor));

        // Delete the unregistered instructor which was inserted
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = 5;");
    }

    @Test
    @DisplayName("Instructor DAO - adding an unregistered instructor to the database")
    public void testCreateUnregisteredInstructor() {
        // Create an unregistered instructor with test data
        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(90,463,(byte)102);

        assertTrue(this.instructorDAO.createUnregisteredInstructor(unregisteredInstructor));

        // Delete the unregistered instructor which was inserted
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = 90;");
    }

    @Test
    @DisplayName("Instructor DAO - adding duplicate unregistered instructor to the database")
    public void testCreateDuplicateUnregisteredInstructor() {
        // Create an unregistered instructor with test data
        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(2,567,(byte)102);

        assertTrue(this.instructorDAO.createUnregisteredInstructor(unregisteredInstructor));
        assertFalse(this.instructorDAO.createUnregisteredInstructor(unregisteredInstructor));

        // Delete the unregistered instructor which was inserted
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = 2;");
    }

    @Test
    @DisplayName("Instructor DAO - Read unregistered record of ID = 55")
    public void testReadUnregisteredInstructor() {
        // Insert an unregistered instructor with test data
        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES
                    (55, 13038, 101);
                """);

        UnregisteredInstructor unregisteredInstructor = this.instructorDAO.readUnregisteredInstructorById(55);

        assertEquals(55, unregisteredInstructor.getInstructorId());
        assertEquals(13038, unregisteredInstructor.getInstructorTempPwHash());
        assertEquals(101, unregisteredInstructor.getDptId());

        // Delete inserted record
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = 55;");
    }

    @Test
    @DisplayName("Instructor DAO - create an unregistered instructor object then read it with DAO methods")
    public void testCreateThenReadUnregisteredInstructor() {
        // Create an unregistered instructor object with test data
        int id = random.nextInt(1, 100);
        int pwHash = random.nextInt(-10000, 10000);
        UnregisteredInstructor newInstructor = new UnregisteredInstructor(id, pwHash, (byte)103);

        assertTrue(this.instructorDAO.createUnregisteredInstructor(newInstructor));

        UnregisteredInstructor instructor = this.instructorDAO.readUnregisteredInstructorById(id);

        assertEquals(id, instructor.getInstructorId());
        assertEquals(pwHash, instructor.getInstructorTempPwHash());
        assertEquals(103, instructor.getDptId());

        // Delete inserted record
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = %d;".formatted(id));
    }


    @Test
    @DisplayName("Instructor DAO - deleting an existing unregistered instructor")
    public void testDeleteValidUnregisteredInstructor() {
        // Insert an unregistered instructor with test data
        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES (34, 3333, 102);
                 """);
        assertTrue(this.instructorDAO.deleteUnregisteredInstructorById(34));
        // Try to delete the same object again
        assertFalse(this.instructorDAO.deleteUnregisteredInstructorById(34));
    }

    @Test
    @DisplayName("Instructor DAO - deleting an invalid unregistered instructor")
    public void testDeleteInvalidUnregisteredInstructor() {
        assertFalse(this.instructorDAO.deleteUnregisteredInstructorById(10));
    }

    @Test
    @DisplayName("get all unregistered Instructors where there exist instructors in the database.")
    public void GetAllUnregisteredInstructorsExist() {

        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES (34, 3333, 102);
                 """);

        jdbcTemplate.update("""
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash, dpt_id)
                VALUES (35, 3333, 102);
                 """);

        List<UnregisteredInstructor> instructors = instructorDAO.getAllUnregisteredInstructors();
        List<Integer> Ids = new ArrayList<>();

        for (UnregisteredInstructor instructor: instructors) {
            Ids.add(instructor.getInstructorId());
        }

        assertTrue(instructors.size() >= 2);
        assertTrue(Ids.contains(34));
        assertTrue(Ids.contains(35));

        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = %d;".formatted(34));
        jdbcTemplate.update("DELETE FROM unregistered_instructor WHERE instructor_id = %d;".formatted(35));

    }

    @Test
    @Disabled
    @DisplayName("get all unregistered instructors where there don't exist instructors in the database.")
    public void GetAllUnregisteredInstructorsNotExist() {

        List<UnregisteredInstructor> instructors = instructorDAO.getAllUnregisteredInstructors();

        assertNotNull(instructors);
        assertTrue(instructors.isEmpty());

    }

    //TODO enforce DB constraint on unregistered_instructor id to be non-negative
}
