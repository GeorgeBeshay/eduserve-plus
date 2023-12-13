package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_uni_objs.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance ( TestInstance.Lifecycle.PER_CLASS )
@SpringBootTest( classes = EspBeApplication.class )
public class CourseDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseDAO courseDAO;

    @BeforeAll
    public void setUp() {
        this.courseDAO = new CourseDAO( jdbcTemplate );
    }

    @Test
    @DisplayName ("Course DAO - try to add new course with repeated id")
    public void addNewCourseRepeatedId() {

        jdbcTemplate.batchUpdate("""
                DELETE FROM course_prereq WHERE course_code = 'CS55';
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);

        Course repeatedCourse = new Course("CS55","paradigms",
                "bla bla", (byte) 1,(byte) 3);

        assertTrue( courseDAO.addNewCourse(course) );
        assertFalse( courseDAO.addNewCourse(repeatedCourse) );

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite size is 0")
    public void addNewCourseWithoutPrereqSize0(){

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        course.setPrerequisite(prereq);

        assertTrue(courseDAO.addNewCourse(course));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite is null")
    public void addNewCourseWithoutPrereqIsNull(){

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);
        course.setPrerequisite( null );
        assertTrue( courseDAO.addNewCourse(course) );

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );

    }

    @Test
    @DisplayName( "Course DAO - try to add new course with valid prerequisite" )
    public void addNewCourseWithValidPrereq(){

        Course pre1 = new Course("CS-1","math1","lkdmf",(byte) 1,(byte) 3);
        Course pre2 = new Course("CS-2","math2","lkdmf",(byte) 1,(byte) 3);
        courseDAO.addNewCourse(pre1);
        courseDAO.addNewCourse(pre2);


        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("CS-1");
        prereq.add("CS-2");
        course.setPrerequisite(prereq);

        assertTrue(courseDAO.addNewCourse(course));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course_prereq WHERE course_code = 'CS55';
                DELETE FROM course WHERE course_code IN ('CS55', 'CS-1', 'CS-2');
                """
        );

    }

    @Test
    @DisplayName ("Course DAO - try to add new course with invalid prerequisite 'roll back'")
    public void addNewCourseWithInvalidPrereq(){

        Course pre1 = new Course("CS-1","math1","lkdmf",(byte) 1,(byte) 3);

        courseDAO.addNewCourse(pre1);

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("CS-1");
        prereq.add("CS-2");
        course.setPrerequisite(prereq);

        assertFalse(courseDAO.addNewCourse(course));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code IN ('CS-1');
                """
        );

    }

    @Test
    @DisplayName("Passing a negative offeringDpt.")
    public void findByOfferingDptInvalidOfferingDpt1() {
        // Arrange - N / A

        // Act
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt((byte) -1);

        // Assert
        assertNull(actualMatchingCourses);
    }

    @Test
    @DisplayName("Passing an offeringDpt that offers no courses.")
    public void findByOfferingDptNoMatchingCourses() {
        // Arrange
        byte offeringDpt = 101;
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", offeringDpt);

        // ACT
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt(offeringDpt);

        // Assert
        assertNotNull(actualMatchingCourses);
        assertTrue(actualMatchingCourses.isEmpty());
    }

    @Test
    @DisplayName("Passing an offeringDpt that offers 1 course.")
    public void findByOfferingDptSingleMatch() {
        // Arrange
        String courseCode = "TEST1";         // recall that the id is defined as varchar(7)
        byte offeringDpt = 101;
        jdbcTemplate.update("DELETE FROM course WHERE course_code = ?", courseCode);
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", offeringDpt);
        jdbcTemplate.update("INSERT INTO course (course_code, offering_dpt) VALUES (?, ?)", courseCode, offeringDpt);

        // Act
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt(offeringDpt);

        // Assert
        assertNotNull(actualMatchingCourses);
        assertEquals(1, actualMatchingCourses.size());
        assertEquals(courseCode, actualMatchingCourses.getFirst().getCourseCode());
        assertEquals(offeringDpt, actualMatchingCourses.getFirst().getOfferingDpt());

        // Clean
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", offeringDpt);
    }

    @Test
    @DisplayName("Passing an offeringDpt that offers multiple, specifically 6, courses.")
    public void findByOfferingDptMultipleMatches() {
        // Arrange
        String[] courseCodes = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5", "TEST6"};
        byte offeringDpt = 101;

        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", offeringDpt);
        for (String courseCode : courseCodes) {
            jdbcTemplate.update("DELETE FROM course WHERE course_code = ?", courseCode);
            jdbcTemplate.update("INSERT INTO course (course_code, offering_dpt) VALUES (?, ?)", courseCode, offeringDpt);
        }

        // Act
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt(offeringDpt);

        // Assert
        assertNotNull(actualMatchingCourses);
        assertEquals(courseCodes.length, actualMatchingCourses.size());
        for (int i = 0 ; i < courseCodes.length ; i++) {
            assertEquals(courseCodes[i], actualMatchingCourses.get(i).getCourseCode());
            assertEquals(offeringDpt, actualMatchingCourses.get(i).getOfferingDpt());
        }

        // Clean
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", offeringDpt);
    }

}
