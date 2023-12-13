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
    private int insertedStudentId1, insertedStudentId2;
    private final int[] setupIds = {1,2,3,4,5};

    @BeforeAll
    public void setUp(){
        this.studentDAO = new StudentDAO(jdbcTemplate);

        jdbcTemplate.batchUpdate("""
                INSERT INTO student (student_id, Student_pw_hash, dpt_id, student_level, gpa, student_name, ssn, bdate, student_address, phone, landline, gender, email)
                VALUES
                    (%d, 456789, 1, 1, 3.5, 'John Doe', '12345678901234', '2000-01-01', '123 Main St', '555-1234', '123-4567', 1, 'john.doe@example.com'),
                    (%d, 987654, 2, 2, 3.2, 'Jane Smith', '98765432101234', '1999-02-15', '456 Oak St', '555-5678', '234-5678', 0, 'jane.smith@example.com'),
                    (%d, 654321, 3, 3, 3.9, 'Bob Johnson', '11112222333344', '1998-05-20', '789 Pine St', '555-8765', '345-6789', 1, 'bob.johnson@example.com');
                  
                INSERT INTO unregistered_student (student_id, student_temp_pw_hash)
                VALUES
                    (%d, 505),
                    (%d, 621);
                """.formatted(setupIds[0], setupIds[1], setupIds[2], setupIds[3], setupIds[4]));
    }

    @AfterAll
    public void closingStatement(){
        jdbcTemplate.batchUpdate("""
                DELETE FROM student WHERE student_id IN (%d, %d, %d, %d);
                DELETE FROM unregistered_student WHERE student_id IN (%d);
                """.formatted(insertedStudentId1, insertedStudentId2, setupIds[0], setupIds[3], setupIds[4]));
        System.out.println("\u001B[32m" + "All Student DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Student DAO - Valid Insertion Test Case")
    public void testCreateStudentSuccess(){
        insertedStudentId1 = random.nextInt(100) + 6;

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(insertedStudentId1);
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn("00001111234587");
        newStudent.setStudentName("Test Student One");

        assertTrue(this.studentDAO.createStudent(newStudent));
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateStudentFailureInvalidId(){
        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(-1);     // setting invalid id
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn("12345678912345");
        newStudent.setStudentName("Invalid Insertion Student");

        assertFalse(this.studentDAO.createStudent(newStudent));
    }

    @Test
    @DisplayName("Student DAO - Invalid Insertion Test Case (id = -1)")
    public void testCreateStudentFailureDuplicateId(){
        insertedStudentId2 = random.nextInt(100) + 6;

        Student newStudent = new Student(); // Create a Student object with test data
        newStudent.setStudentId(insertedStudentId2);
        newStudent.setDepartmentId((byte) 1);
        newStudent.setSsn("98765423112345");
        newStudent.setStudentName("Test Student Two");

        assertTrue(this.studentDAO.createStudent(newStudent));
        assertFalse(this.studentDAO.createStudent(newStudent));     // duplicate record
    }

    @Test
    @DisplayName("Student DAO - Read record of ID = 1")
    public void testReadStudentByValidId() {
        Student student = this.studentDAO.readStudentById(setupIds[0]);

        assertNotNull(student);
        assertEquals(setupIds[0], student.getStudentId());
        assertEquals("John Doe", student.getStudentName());
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
        assertTrue(this.studentDAO.deleteStudentById(setupIds[2]));
    }

    @Test
    @DisplayName("Student DAO - delete invalid student with ID = 19")
    public void testDeleteStudentByInvalidId() {
        assertFalse(this.studentDAO.deleteStudentById(19));
    }

    @Test
    @DisplayName("Student DAO - delete valid student with ID = 2, then try to delete the same student again")
    public void testDeleteStudentTwice() {
        assertTrue(this.studentDAO.deleteStudentById(setupIds[1]));
        assertFalse(this.studentDAO.deleteStudentById(setupIds[1]));
    }


    @Test
    @DisplayName("Student DAO - Sign up a student by moving from unregistered students to complete students table")
    public void testSignUpValidStudent() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(setupIds[3]);
        registeredStudent.setStudentPwHash(9832);
        registeredStudent.setStudentName("Signed Up Student");
        registeredStudent.setDepartmentId((byte) 3);
        assertTrue(this.studentDAO.signUpStudent(setupIds[3], 505, registeredStudent));
    }

    @Test
    @DisplayName("Student DAO - Sign up a student whose id is not in the unregistered table")
    public void testSignUpInvalidStudent() {
        Student unregisteredStudent = new Student(); // Create a student object with test data
        unregisteredStudent.setStudentId(155);
        unregisteredStudent.setStudentPwHash(716);
        unregisteredStudent.setStudentName("Unregistered Student");
        unregisteredStudent.setDepartmentId((byte) 2);
        assertFalse(this.studentDAO.signUpStudent(155, 754, unregisteredStudent));
    }

    @Test
    @DisplayName("Student DAO - Sign up a student with a valid id but invalid temporary password")
    public void testSignUpStudentInvalidPassword() {
        Student registeredStudent = new Student(); // Create a student object with test data
        registeredStudent.setStudentId(setupIds[4]);
        registeredStudent.setStudentPwHash(4001);
        registeredStudent.setStudentName("Fake Student");
        registeredStudent.setDepartmentId((byte) 1);
        assertFalse(this.studentDAO.signUpStudent(setupIds[4], 414, registeredStudent));
    }

}
