package edu.esp.services;

import edu.esp.be.EspBeApplication;
import edu.esp.database.daos.CourseDAO;
import edu.esp.database.daos.InstructorDAO;
import edu.esp.database.daos.StudentDAO;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class StudentServicesTests {

    private final JdbcTemplate jdbcTemplate;
    private StudentServices studentServices;

    @Autowired
    public StudentServicesTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentServices = new StudentServices(jdbcTemplate);
    }

    @BeforeEach
    public void setUpBeforeEach() {
        this.studentServices = new StudentServices(jdbcTemplate);
    }

    @Test
    public void testGetCourseRegistrationSetup() {
        // Arrange

        // department
        byte dptId = 101;
        String insertingDepartmentSql = "INSERT INTO department (dpt_id) VALUES (?);";
        assertEquals(1, jdbcTemplate.update(insertingDepartmentSql, dptId));

        int studentId = 99;
        Student student = new Student();
        student.setStudentId(studentId);
        student.setDptId(dptId);

        String courseCode1 = "TEST1";
        String courseName1 = "COURSE TEST 1";
        String courseDescription1 = "TEST COURSE DESCRIPTION 1";
        byte creditHrs1 = 4;
        Course c1 = new Course(courseCode1, courseName1, courseDescription1, dptId, creditHrs1);

        String courseCode2 = "TEST2";
        String courseName2 = "COURSE TEST 2";
        String courseDescription2 = "TEST COURSE DESCRIPTION 2";
        byte creditHrs2 = 3;
        Course c2 = new Course(courseCode2, courseName2, courseDescription2, dptId, creditHrs2);

        String courseCode3 = "TEST3";
        String courseName3 = "COURSE TEST 3";
        String courseDescription3 = "TEST COURSE DESCRIPTION 3";
        byte creditHrs3 = 3;
        Course c3 = new Course(courseCode3, courseName3, courseDescription3, dptId, creditHrs3);

        int instructorId = 99;
        Instructor instructor = new Instructor();
        instructor.setInstructorId(instructorId);
        instructor.setDptId(dptId);

        // needed DAOs
        CourseDAO courseDAO = new CourseDAO(jdbcTemplate);
        StudentDAO studentDAO = new StudentDAO(this.jdbcTemplate);
        InstructorDAO instructorDAO = new InstructorDAO(this.jdbcTemplate);

        assertTrue(courseDAO.addNewCourse(c1));
        assertTrue(courseDAO.addNewCourse(c2));
        assertTrue(courseDAO.addNewCourse(c3));
        assertTrue(studentDAO.createStudent(student));
        assertTrue(instructorDAO.createInstructor(instructor));

        // instructs relation
        byte season = 0;
        String academicYear = "2023";
        String instructsSql =
                "INSERT INTO instructs (instructor_id, course_code, season, academic_year) " +
                "VALUES (?, ?, ?, ?);";

        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode1, season, academicYear));
        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode2, season, academicYear));
        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode3, season, academicYear));



        int availableNumberOfCreditHours = 21;

        Map<String, Object> expectedCourseregistrationSetupMap = new HashMap<>();
        List<Course> availableCourses = new ArrayList<>();
        availableCourses.add(c1);
        availableCourses.add(c2);
        availableCourses.add(c3);
        expectedCourseregistrationSetupMap.put("availableCourses", availableCourses);
        expectedCourseregistrationSetupMap.put("availableCreditHours", availableNumberOfCreditHours);

        // Act
        Map<String, Object> actualCourseregistrationSetupMap = studentServices.getCourseRegistrationSetup(studentId);

        // Assert
        assertNotNull(actualCourseregistrationSetupMap);
        assertTrue(actualCourseregistrationSetupMap.containsKey("availableCourses"));
        assertTrue(actualCourseregistrationSetupMap.containsKey("availableCreditHours"));
        assertEquals(expectedCourseregistrationSetupMap, actualCourseregistrationSetupMap);

        // Clean
        assertEquals(3, jdbcTemplate.update("DELETE FROM instructs WHERE instructor_id = ?;",
                instructorId));
        assertEquals(3, jdbcTemplate.update("DELETE FROM course WHERE course_code IN (?, ?, ?);",
                courseCode1, courseCode2, courseCode3));
        assertEquals(1, jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = ?", instructorId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM student WHERE student_id = ?", studentId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", dptId));
    }

    @Test
    public void testRegisterCourses() {
        // Arrange

        // department
        byte dptId = 101;
        String insertingDepartmentSql = "INSERT INTO department (dpt_id) VALUES (?);";
        assertEquals(1, jdbcTemplate.update(insertingDepartmentSql, dptId));

        int studentId = 99;
        Student student = new Student();
        student.setStudentId(studentId);
        student.setDptId(dptId);

        String courseCode1 = "TEST1";
        String courseName1 = "COURSE TEST 1";
        String courseDescription1 = "TEST COURSE DESCRIPTION 1";
        byte creditHrs1 = 4;
        Course c1 = new Course(courseCode1, courseName1, courseDescription1, dptId, creditHrs1);

        String courseCode2 = "TEST2";
        String courseName2 = "COURSE TEST 2";
        String courseDescription2 = "TEST COURSE DESCRIPTION 2";
        byte creditHrs2 = 3;
        Course c2 = new Course(courseCode2, courseName2, courseDescription2, dptId, creditHrs2);

        String courseCode3 = "TEST3";
        String courseName3 = "COURSE TEST 3";
        String courseDescription3 = "TEST COURSE DESCRIPTION 3";
        byte creditHrs3 = 3;
        Course c3 = new Course(courseCode3, courseName3, courseDescription3, dptId, creditHrs3);

        int instructorId = 99;
        Instructor instructor = new Instructor();
        instructor.setInstructorId(instructorId);
        instructor.setDptId(dptId);

        // needed DAOs
        CourseDAO courseDAO = new CourseDAO(jdbcTemplate);
        StudentDAO studentDAO = new StudentDAO(this.jdbcTemplate);
        InstructorDAO instructorDAO = new InstructorDAO(this.jdbcTemplate);

        assertTrue(courseDAO.addNewCourse(c1));
        assertTrue(courseDAO.addNewCourse(c2));
        assertTrue(courseDAO.addNewCourse(c3));
        assertTrue(studentDAO.createStudent(student));
        assertTrue(instructorDAO.createInstructor(instructor));

        // instructs relation
        byte season = 0;
        String academicYear = "2023";
        String instructsSql =
                "INSERT INTO instructs (instructor_id, course_code, season, academic_year) " +
                        "VALUES (?, ?, ?, ?);";

        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode1, season, academicYear));
        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode2, season, academicYear));
        assertEquals(1, jdbcTemplate.update(instructsSql, instructorId, courseCode3, season, academicYear));

        List<String> selectedCourses = new ArrayList<>();
        selectedCourses.add(courseCode1);
        selectedCourses.add(courseCode2);
        selectedCourses.add(courseCode3);

        Map<String, Object> registrationMap = new HashMap<>();
        registrationMap.put("studentId", studentId);
        registrationMap.put("selectedCourses", selectedCourses);
        registrationMap.put("totalNumberOfHours", c1.getCreditHrs() + c2.getCreditHrs() + c3.getCreditHrs());

        int expectedRegisteredCourses = 3;

        // Act
        int actualRegisteredCourses = studentServices.registerCourses(registrationMap);
        assertEquals(expectedRegisteredCourses, actualRegisteredCourses);

        // Assert

        // Clean
        assertEquals(3, jdbcTemplate.update("DELETE FROM grades WHERE course_code IN (?, ?, ?);", courseCode1, courseCode2, courseCode3));
        assertEquals(3, jdbcTemplate.update("DELETE FROM instructs WHERE instructor_id = ?;",
                instructorId));
        assertEquals(3, jdbcTemplate.update("DELETE FROM course WHERE course_code IN (?, ?, ?);",
                courseCode1, courseCode2, courseCode3));
        assertEquals(1, jdbcTemplate.update("DELETE FROM instructor WHERE instructor_id = ?", instructorId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM student WHERE student_id = ?", studentId));
        assertEquals(1, jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", dptId));
    }

}
