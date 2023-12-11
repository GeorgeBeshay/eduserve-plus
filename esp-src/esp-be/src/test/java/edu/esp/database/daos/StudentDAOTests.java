package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class StudentDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentDAO studentDAO;
    private final Random random = new Random();

    @BeforeAll
    public void setUp(){
        this.studentDAO = new StudentDAO(jdbcTemplate);
    }

    @AfterAll
    public void closingStatement(){
        System.out.println("\u001B[32m" + "All Student DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Student DAO - Valid Insertion Test Case")
    public void testCreateStudentSuccess(){
        int insertedStudentId = random.nextInt(1, 100);

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(insertedStudentId);
        newStudent.setDptId((byte) 1);
        newStudent.setSsn("00001111234587");
        newStudent.setStudentName("Test Student");
        newStudent.setBdate("2000-01-02");
        newStudent.setGender(true);
        newStudent.setEmail("testemail@blabla.com");

        assertTrue(this.studentDAO.createStudent(newStudent));

        jdbcTemplate.update("DELETE FROM student WHERE student_id = %d;".formatted(insertedStudentId));
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateStudentFailureInvalidId(){
        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(-1);     // setting invalid id
        newStudent.setDptId((byte) 1);
        newStudent.setSsn("12345678912345");
        newStudent.setStudentName("Invalid Insertion Student");
        newStudent.setBdate("2003-05-02");
        newStudent.setGender(false);
        newStudent.setEmail("testemail@nenene.com");

        assertFalse(this.studentDAO.createStudent(newStudent));
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (duplicate id)")
    public void testCreateStudentFailureDuplicateId(){
        int insertedStudentId = random.nextInt(1, 100);

        // Create two student objects with test data and same id
        Student newStudent1 = new Student(
                insertedStudentId, 32145, (byte)1, (byte)2, 2.9F,
                "Test Student", "98765423112345", "2002-08-02", "Some address 123",
                null, null, true, null
        );
        Student newStudent2 = new Student(
                insertedStudentId, -145, (byte)3, (byte)3, 3.4F,
                "DupID Test Student", "98760003112345", "2000-08-05", "Some address 403",
                null, null, false, null
        );

        assertTrue(this.studentDAO.createStudent(newStudent1));
        assertFalse(this.studentDAO.createStudent(newStudent2)); // duplicate id record

        // Delete the student used in the test
        jdbcTemplate.update("DELETE FROM student WHERE student_id = %d;".formatted(insertedStudentId));
    }

    @Test
    @DisplayName("Student DAO - Read record of ID = 1")
    public void testReadStudentByValidId() {
        // Insert a student with ID = 1 into the database
        jdbcTemplate.update("""
                INSERT INTO student (student_id, Student_pw_hash, dpt_id, student_level, gpa, student_name, ssn, bdate, student_address, phone, landline, gender, email)
                VALUES (1, 456789, 1, 1, 3.5, 'John Doe', '12345678901234', '2000-01-01', '123 Main St', '555-1234', '123-4567', 1, 'john.doe@example.com');
                """);

        Student student = this.studentDAO.readStudentById(1);

        assertNotNull(student);
        assertEquals(1, student.getStudentId());
        assertEquals(456789, student.getStudentPwHash());
        assertEquals(1, student.getDptId());
        assertEquals(1, student.getStudentLevel());
        assertEquals(3.5, student.getGpa());
        assertEquals("John Doe", student.getStudentName());
        assertEquals("12345678901234", student.getSsn());
        assertEquals("2000-01-01", student.getBdate());
        assertEquals("123 Main St", student.getStudentAddress());
        assertEquals("555-1234", student.getPhone());
        assertEquals("123-4567", student.getLandline());
        assertTrue(student.isMale());
        assertEquals("john.doe@example.com", student.getEmail());

        // Delete the student used in the test
        jdbcTemplate.update("DELETE FROM student WHERE student_id = 1;");
    }

    @Test
    @DisplayName("Student DAO - Read invalid record of ID = -1")
    public void testReadStudentByInvalidId() {
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
    public void testDeleteStudentByValidId() {
        // Insert student with ID = 3
        jdbcTemplate.update("""
                INSERT INTO student (student_id, Student_pw_hash, dpt_id, student_level, gpa, student_name, ssn, bdate, student_address, phone, landline, gender, email)
                VALUES (3, 654321, 3, 3, 3.9, 'Bob Johnson', '11112222333344', '1998-05-20', '789 Pine St', '555-8765', '345-6789', 1, 'bob.johnson@example.com');
                """);

        assertTrue(this.studentDAO.deleteStudentById(3));
    }

    @Test
    @DisplayName("Student DAO - delete invalid student with ID = 19")
    public void testDeleteStudentByInvalidId() {
        assertFalse(this.studentDAO.deleteStudentById(19));
    }

    @Test
    @DisplayName("Student DAO - delete valid student with ID = 2, then try to delete the same student again")
    public void testDeleteStudentTwice() {
        // Insert student with ID = 2
        jdbcTemplate.update("""
                INSERT INTO student (student_id, Student_pw_hash, dpt_id, student_level, gpa, student_name, ssn, bdate, student_address, phone, landline, gender, email)
                VALUES (2, 987654, 2, 2, 3.2, 'Jane Smith', '98765432101234', '1999-02-15', '456 Oak St', '555-5678', '234-5678', 0, 'jane.smith@example.com');
                """);

        assertTrue(this.studentDAO.deleteStudentById(2));
        assertFalse(this.studentDAO.deleteStudentById(2));
    }


    @Test
    @DisplayName("Student DAO - Sign up a student by moving from unregistered students to complete students table")
    public void testSignUpValidStudent() {
        // Insert a student into the unregistered table
        jdbcTemplate.update("""
                INSERT INTO unregistered_student (student_id, student_temp_pw_hash)
                VALUES (4, 505);
                """);

        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(4);
        registeredStudent.setStudentPwHash(9832);
        registeredStudent.setStudentName("Signed Up Student");
        registeredStudent.setDptId((byte) 3);
        registeredStudent.setSsn("12345678912345");
        registeredStudent.setBdate("2003-05-02");
        registeredStudent.setGender(false);
        registeredStudent.setEmail("signeduptest@blabla.com");

        assertTrue(this.studentDAO.signUpStudent(4, 505, registeredStudent));

        // Delete the student used in the test
        jdbcTemplate.update("DELETE FROM student WHERE student_id = 4;");
    }

    @Test
    @DisplayName("Student DAO - Sign up a student whose id is not in the unregistered table")
    public void testSignUpInvalidStudent() {
        Student unregisteredStudent = new Student(); // Create a student object with test data
        unregisteredStudent.setStudentId(155);
        unregisteredStudent.setStudentPwHash(716);
        unregisteredStudent.setStudentName("Unregistered Student");
        unregisteredStudent.setDptId((byte) 2);
        unregisteredStudent.setSsn("12340078902340");
        unregisteredStudent.setBdate("2002-05-02");
        unregisteredStudent.setGender(true);
        unregisteredStudent.setEmail("unregistered@blabla.com");

        assertFalse(this.studentDAO.signUpStudent(155, 754, unregisteredStudent));
    }

    @Test
    @DisplayName("Student DAO - Sign up a student with a valid id but invalid temporary password")
    public void testSignUpStudentInvalidPassword() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(5);
        registeredStudent.setStudentPwHash(4001);
        registeredStudent.setStudentName("Fake Student");
        registeredStudent.setDptId((byte) 1);
        registeredStudent.setSsn("12343378992344");
        registeredStudent.setBdate("2000-05-02");
        registeredStudent.setGender(false);
        registeredStudent.setEmail("fakeperson@blabla.com");

        assertFalse(this.studentDAO.signUpStudent(5, 414, registeredStudent));
    }

}
