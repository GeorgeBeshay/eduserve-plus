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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class CourseDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CourseDAO CourseDAO;

    @BeforeAll
    public void setUp() {
        this.CourseDAO = new CourseDAO(jdbcTemplate);
    }

    @Test
    @DisplayName("Course DAO - try to add new course with repeated id")
    public void addNewCourseRepeatedId(){

        jdbcTemplate.batchUpdate("""
                DELETE FROM course_prereq WHERE course_code = 'CS55';
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);

        Course repeatedCourse = new Course("CS55","paradigms",
                "bla bla", (byte) 1,(byte) 3);

        assertTrue(CourseDAO.addNewCourse(course,null));
        assertFalse(CourseDAO.addNewCourse(repeatedCourse,null));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );
    }

    @Test
    @DisplayName("Course DAO - try to add new course without prerequisite (prerequisite size is 0")
    public void addNewCourseWithoutPrereqSize0(){

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);
        List<String> prereq = new ArrayList<>();
        assertTrue(CourseDAO.addNewCourse(course,prereq));
        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );
    }

    @Test
    @DisplayName("Course DAO - try to add new course without prerequisite (prerequisite is null")
    public void addNewCourseWithoutPrereqIsNull(){

        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);
        assertTrue(CourseDAO.addNewCourse(course,null));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );

    }

    @Test
    @DisplayName("Course DAO - try to add new course with valid prerequisite")
    public void addNewCourseWithValidPrereq(){

        Course pre1 = new Course("CS-1","math1","lkdmf",(byte) 1,(byte) 3);
        Course pre2 = new Course("CS-2","math2","lkdmf",(byte) 1,(byte) 3);
        CourseDAO.addNewCourse(pre1,null);
        CourseDAO.addNewCourse(pre2,null);


        Course course = new Course("CS55","programming1",
                "bla bla", (byte) 1,(byte) 3);
        List<String> prereq = new ArrayList<>();
        prereq.add("CS-1");
        prereq.add("CS-2");
        assertTrue(CourseDAO.addNewCourse(course,prereq));

        jdbcTemplate.batchUpdate("""
                DELETE FROM course_prereq WHERE course_code = 'CS55';
                DELETE FROM course WHERE course_code IN ('CS55', 'CS-1', 'CS-2');
                """
        );

    }

}
