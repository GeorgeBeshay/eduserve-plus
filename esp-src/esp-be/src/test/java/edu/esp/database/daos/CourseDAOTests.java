package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import org.junit.jupiter.api.*;
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
        // TODO insert testing departments
        this.courseDAO = new CourseDAO( jdbcTemplate );
    }

    @AfterAll
    public void cleanUp() {
        // TODO delete testing departments
    }

    @Test
    @DisplayName ("Course DAO - try to add new course with repeated id")
    public void addNewCourseRepeatedId() {

        Course course = new Course("TEST1","programming1",
                "bla bla", (byte) 1,(byte) 3);

        Course repeatedCourse = new Course("TEST1","paradigms",
                "bla bla", (byte) 1,(byte) 3);

        assertTrue( courseDAO.addNewCourse(course) );
        assertFalse( courseDAO.addNewCourse(repeatedCourse) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST1';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite size is 0")
    public void addNewCourseWithPrerequisitesSize0(){

        Course course = new Course("TEST2","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> preReq = new ArrayList<>();
        course.setPrerequisite(preReq);

        assertTrue(courseDAO.addNewCourse(course));

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST2';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite is null")
    public void addNewCourseWithNullPrerequisites(){

        Course course = new Course("TEST3","programming1",
                "bla bla", (byte) 1,(byte) 3);
        course.setPrerequisite( null );

        assertTrue( courseDAO.addNewCourse(course) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST3';");
    }

    @Test
    @DisplayName( "Course DAO - try to add new course with valid prerequisite" )
    public void addNewCourseWithValidPrerequisites(){

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 1,(byte) 3);
        Course pre2 = new Course("PRE2","math2","lkdmf",(byte) 1,(byte) 3);
        courseDAO.addNewCourse(pre1);
        courseDAO.addNewCourse(pre2);


        Course course = new Course("TEST4","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("PRE1");
        prereq.add("PRE2");
        course.setPrerequisite(prereq);

        assertTrue(courseDAO.addNewCourse(course));

        // Database trigger will remove ('TEST4','PRE1') and ('TEST4','PRE2') from prerequisite table
        jdbcTemplate.batchUpdate("DELETE FROM course WHERE course_code IN ('TEST4', 'PRE1', 'PRE2');");

    }

    @Test
    @DisplayName ("Course DAO - try to add new course with invalid prerequisite 'roll back'")
    public void addNewCourseWithInvalidPrerequisites(){

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 1,(byte) 3);

        courseDAO.addNewCourse(pre1);

        Course course = new Course("TEST5","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("PRE1");
        prereq.add("PRE2"); // Does not exist
        course.setPrerequisite(prereq);

        assertFalse(courseDAO.addNewCourse(course));

        //TODO Roll back the whole action on prerequisites too.
        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code IN ('PRE1');
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
    @DisplayName("Passing a null offeringDpt.")
    public void findByOfferingDptInvalidOfferingDpt2() {
        // Arrange
        Byte offeringDpt = null;

        // Act
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt(offeringDpt);

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
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", offeringDpt);
        jdbcTemplate.update("INSERT INTO department (dpt_id, dpt_name) VALUES (?, ?)", offeringDpt, "TEST DEPARTMENT");
        jdbcTemplate.update("INSERT INTO course (course_code, offering_dpt) VALUES (?, ?)", courseCode, offeringDpt);

        // Act
        List<Course> actualMatchingCourses = courseDAO.findByOfferingDpt(offeringDpt);

        // Assert
        assertNotNull(actualMatchingCourses);
        assertEquals(1, actualMatchingCourses.size());
        assertEquals(courseCode, actualMatchingCourses.get(0).getCourseCode());
        assertEquals(offeringDpt, actualMatchingCourses.get(0).getOfferingDpt());

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
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", offeringDpt);
        jdbcTemplate.update("INSERT INTO department (dpt_id, dpt_name) VALUES (?, ?)", offeringDpt, "TEST DEPARTMENT");
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

    @Test
    @DisplayName("Get All Courses not empty")
    public void GetAllCoursesNotEmpty() {
        DBFacadeImp db = new DBFacadeImp(jdbcTemplate);
        db.addNewCourse(new Course("TEST1","TEST","ay habal",(byte)1,(byte)2));
        db.addNewCourse(new Course("TEST2","TEST","ay habal",(byte)1,(byte)2));

        List<Course> courseList = db.getAllCourses();
        assertEquals(courseList.size(),2);
        assertEquals(courseList.get(0).getCourseCode(),"TEST1");
        assertEquals(courseList.get(1).getCourseCode(),"TEST2");


        jdbcTemplate.update("DELETE FROM course WHERE course_name = 'TEST'");

    }

    @Test
    @DisplayName("Get All Courses empty")
    public void GetAllCoursesEmpty() {
        DBFacadeImp db = new DBFacadeImp(jdbcTemplate);

        List<Course> courseList = db.getAllCourses();

        assertNotNull(courseList);
        assertEquals(courseList.size(),0);

    }

}
