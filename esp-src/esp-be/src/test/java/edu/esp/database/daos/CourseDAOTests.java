package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_uni_objs.Course;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance ( TestInstance.Lifecycle.PER_CLASS )
@SpringBootTest( classes = EspBeApplication.class )
public class CourseDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseDAO CourseDAO;

    @BeforeAll
    public void setUp() {
        // TODO insert testing departments
        this.CourseDAO = new CourseDAO( jdbcTemplate );
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

        assertTrue( CourseDAO.addNewCourse(course) );
        assertFalse( CourseDAO.addNewCourse(repeatedCourse) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST1';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite size is 0")
    public void addNewCourseWithPrerequisitesSize0(){

        Course course = new Course("TEST2","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> preReq = new ArrayList<>();
        course.setPrerequisite(preReq);

        assertTrue(CourseDAO.addNewCourse(course));

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST2';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite is null")
    public void addNewCourseWithNullPrerequisites(){

        Course course = new Course("TEST3","programming1",
                "bla bla", (byte) 1,(byte) 3);
        course.setPrerequisite( null );

        assertTrue( CourseDAO.addNewCourse(course) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST3';");
    }

    @Test
    @DisplayName( "Course DAO - try to add new course with valid prerequisite" )
    public void addNewCourseWithValidPrerequisites(){

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 1,(byte) 3);
        Course pre2 = new Course("PRE2","math2","lkdmf",(byte) 1,(byte) 3);
        CourseDAO.addNewCourse(pre1);
        CourseDAO.addNewCourse(pre2);


        Course course = new Course("TEST4","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("PRE1");
        prereq.add("PRE2");
        course.setPrerequisite(prereq);

        assertTrue(CourseDAO.addNewCourse(course));

        // Database trigger will remove ('TEST4','PRE1') and ('TEST4','PRE2') from prerequisite table
        jdbcTemplate.batchUpdate("DELETE FROM course WHERE course_code IN ('TEST4', 'PRE1', 'PRE2');");

    }

    @Test
    @DisplayName ("Course DAO - try to add new course with invalid prerequisite 'roll back'")
    public void addNewCourseWithInvalidPrerequisites(){

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 1,(byte) 3);

        CourseDAO.addNewCourse(pre1);

        Course course = new Course("TEST5","programming1",
                "bla bla", (byte) 1,(byte) 3);

        List<String> prereq = new ArrayList<>();
        prereq.add("PRE1");
        prereq.add("PRE2"); // Does not exist
        course.setPrerequisite(prereq);

        assertFalse(CourseDAO.addNewCourse(course));

        //TODO Roll back the whole action on prerequisites too.
        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code IN ('PRE1');
                """
        );

    }

}
