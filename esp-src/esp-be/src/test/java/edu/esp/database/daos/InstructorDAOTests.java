package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private int insertedInstructorId1;
    private final int[] setupIds = {1,2,3,4,5};

    @BeforeAll
    public void setUp() {
        this.instructorDAO = new InstructorDAO(jdbcTemplate);

        jdbcTemplate.batchUpdate("""
                INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
                VALUES
                  (%d, 135790, 1, 'Prof. Anderson', '555-4321', 'prof.anderson@example.com', 'Monday 10am-12pm, Wednesday 2pm-4pm'),
                  (%d, 246801, 2, 'Dr. Thompson', '555-6789', 'dr.thompson@example.com', 'Tuesday 9am-11am, Thursday 1pm-3pm'),
                  (%d, 987123, 3, 'Prof. Williams', '555-9876', 'prof.williams@example.com', 'Friday 3pm-5pm, Saturday 10am-12pm');
                            
                INSERT INTO unregistered_instructor (instructor_id, Instructor_temp_pw_hash)
                VALUES
                    (%d, 999),
                    (%d, 120);
                """.formatted(setupIds[0],setupIds[1],setupIds[2],setupIds[3], setupIds[4]));
    }

    @AfterAll
    public void closingStatement(){
        jdbcTemplate.batchUpdate("""
                DELETE FROM instructor WHERE instructor_id IN (%d, %d, %d);
                DELETE FROM unregistered_instructor WHERE instructor_id IN (%d);
                """.formatted(setupIds[0], setupIds[3], insertedInstructorId1, setupIds[4]));
        System.out.println("\u001B[32m" + "All Instructor DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Instructor DAO - Valid Insertion Test Case")
    public void testCreateInstructorSuccess(){
        insertedInstructorId1 = random.nextInt(100) + 6;
        Instructor newInstructor = new Instructor(); // Create an instructor object with test data
        newInstructor.setInstructorId(insertedInstructorId1);
        newInstructor.setInstructorPwHash(354);
        newInstructor.setDptId((byte) 1);
        newInstructor.setInstructorName("Test Instructor One");

        assertTrue(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateInstructorFailureInvalidId(){
        Instructor newInstructor = new Instructor(); // Create a Student object with test data

        newInstructor.setInstructorId(-1);
        newInstructor.setDptId((byte) 1);
        newInstructor.setInstructorName("Invalid Insertion Instructor");

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Invalid Insertion Test Case with duplicate id")
    public void testCreateInstructorFailureDuplicateId(){

        Instructor newInstructor = new Instructor();
        newInstructor.setInstructorId(setupIds[0]);     // setting duplicate id
        newInstructor.setDptId((byte) 1);
        newInstructor.setInstructorName("Duplicate Insertion Instructor");

        assertFalse(this.instructorDAO.createInstructor(newInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Read record of ID = 1")
    public void testReadInstructorByValidId() {
        Instructor instructor = this.instructorDAO.readInstructorById(setupIds[0]);

        assertNotNull(instructor);
        assertEquals(setupIds[0], instructor.getInstructorId());
        assertEquals("Prof. Anderson", instructor.getInstructorName());
    }

    @Test
    @DisplayName("Instructor DAO - Read invalid record of ID = -1")
    public void testReadInstructorByInvalidId() {
        Instructor instructor = this.instructorDAO.readInstructorById(-1);
        assertNull(instructor);
    }

    @Test
    @DisplayName("Instructor DAO - Select all instructors")
    public void testSelectAll(){
        List<Instructor> instructors = this.instructorDAO.SelectAll();
        //we iterate over the returned list to assert that it reads well
        System.out.println(instructors.toString());
        assertEquals(2,instructors.get(1).getInstructorId());
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 2")
    public void testDeleteInstructorByValidId() {
        assertTrue(this.instructorDAO.deleteInstructorById(setupIds[1]));
    }

    @Test
    @DisplayName("Instructor DAO - delete invalid instructor with ID = 10")
    public void testDeleteInstructorByInvalidId() {
        assertFalse(this.instructorDAO.deleteInstructorById(10));
    }

    @Test
    @DisplayName("Instructor DAO - delete valid instructor with ID = 3, then try to delete the same instructor again")
    public void testDeleteInstructorTwice() {
        assertTrue(this.instructorDAO.deleteInstructorById(setupIds[2]));
        assertFalse(this.instructorDAO.deleteInstructorById(setupIds[2]));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor by moving from unregistered instructors to complete instructors table")
    public void testSignUpValidInstructor() {
        Instructor registeredInstructor = new Instructor(); // Create an instructor object with test data
        registeredInstructor.setInstructorId(setupIds[3]);
        registeredInstructor.setInstructorPwHash(9847);
        registeredInstructor.setInstructorName("Signed Up Instructor");
        registeredInstructor.setDptId((byte) 2);
        assertTrue(this.instructorDAO.signUpInstructor(setupIds[3], 999, registeredInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor whose id is not in the unregistered table")
    public void testSignUpInvalidInstructor() {
        Instructor unregisteredInstructor = new Instructor(); // Create an instructor object with test data
        unregisteredInstructor.setInstructorId(14);
        unregisteredInstructor.setInstructorPwHash(1001);
        unregisteredInstructor.setInstructorName("Unregistered Instructor");
        unregisteredInstructor.setDptId((byte) 2);
        assertFalse(this.instructorDAO.signUpInstructor(14, 987, unregisteredInstructor));
    }

    @Test
    @DisplayName("Instructor DAO - Sign up an instructor with a valid id but invalid temporary password")
    public void testSignUpInstructorInvalidPassword() {
        Instructor registeredInstructor = new Instructor(); // Create an instructor object with test data
        registeredInstructor.setInstructorId(setupIds[4]);
        registeredInstructor.setInstructorPwHash(444);
        registeredInstructor.setInstructorName("Fake Instructor");
        registeredInstructor.setDptId((byte) 3);
        assertFalse(this.instructorDAO.signUpInstructor(setupIds[4], 404, registeredInstructor));
    }
    @Test
    @DisplayName("Instructor DAO - adding an unregistered instructor to the database")
    public void testAddingUnregisteredInstructor(){
        this.instructorDAO.deleteUnregisteredInstructorById(9999);
        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(9999,463);
        assertTrue(this.instructorDAO.addUnregisteredInstructors(unregisteredInstructor));
        this.instructorDAO.deleteUnregisteredInstructorById(9999);
    }
    @Test
    @DisplayName("Instructor DAO - adding duplicate unregistered instructor to the database")
    public void testAddingDuplicateUnregisteredInstructor(){
        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(2,567);
        this.instructorDAO.addUnregisteredInstructors(unregisteredInstructor);
        assertFalse(this.instructorDAO.addUnregisteredInstructors(unregisteredInstructor));
        this.instructorDAO.deleteUnregisteredInstructorById(2);
    }

//    @Test
//    @DisplayName("Instructor DAO - adding unregistered instructor with invalid ID = -1") //TODO enforce DB constraint on unregistered_instructor id to be non-negative
//    public void testAddingInvalidUnregisteredInstructor(){
//        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(-1,4555);
//        assertFalse(this.instructorDAO.addUnregisteredInstructors(unregisteredInstructor));
//    }
    @Test
    @DisplayName("Instructor DAO - deleting an existing unregistered instructor")
    public void testDeletingAnExistingUnregisteredInstructor(){
        UnregisteredInstructor unregisteredInstructor = new UnregisteredInstructor(34,3333);
        this.instructorDAO.addUnregisteredInstructors(unregisteredInstructor);
        assertTrue(this.instructorDAO.deleteUnregisteredInstructorById(34));
    }
    @Test
    @DisplayName("Instructor DAO - deleting an invalid unregistered instructor")
    public void testDeletingAnInvalidUnregisteredInstructor(){
        assertFalse(this.instructorDAO.deleteUnregisteredInstructorById(10));
    }

}
