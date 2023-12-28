package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_uni_objs.Grade;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@SpringBootTest( classes = EspBeApplication.class )
public class GradeDAOTests{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GradeDAO gradeDAO;

    @BeforeAll
    public void setUp() {
        this.gradeDAO = new GradeDAO( jdbcTemplate );

        jdbcTemplate.update("""
                INSERT INTO department (dpt_id, dpt_name)
                VALUES
                  (101, 'Test DPT 1');
                """);


        jdbcTemplate.update("""
                INSERT INTO student (student_id, dpt_id, ssn)
                VALUES
                  (1, 101, 1),
                  (2, 101, 2);
                """);

        jdbcTemplate.update("""
                INSERT INTO course (course_code, course_name, course_description, offering_dpt, credit_hrs)
                VALUES
                  ('TEST1', 'TEST1', 'description', 101, 3),
                  ('TEST2', 'TEST2', 'description', 101, 3),
                  ('TEST3', 'TEST3', 'description', 101, 3);
                """);


    }

    @AfterAll
    public void cleanUp() {

        jdbcTemplate.update("DELETE FROM student WHERE dpt_id = 101");
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = 101");
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id = 101;");

    }

    @Test
    @DisplayName("Withdraw from multiple courses where all is allowed")
    public void withdrawMultipleCoursesAllAllowed(){

        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);

        // All the grades that inserted has passed is NULL ... it means that the student has the course in progress
        jdbcTemplate.update("""
                INSERT INTO grades (course_code, student_id, season, academic_year)
                VALUES
                  ('TEST1', 1, ?, ?),
                  ('TEST2', 1, ?, ?),
                  ('TEST3', 1, ?, ?);
                """, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear);

        List<Grade> grades = gradeDAO.getAllGrades();

        assert currentSeason != null;

        Grade g1 = new Grade("TEST1", 1, currentSeason.byteValue(), currentYear);
        Grade g2 = new Grade("TEST2", 1, currentSeason.byteValue(), currentYear);
        Grade g3 = new Grade("TEST3", 1, currentSeason.byteValue(), currentYear);

        boolean c1 = grades.contains(g1);
        boolean c2 = grades.contains(g2);
        boolean c3 = grades.contains(g3);

        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("TEST1","TEST1","description",(byte) 101,(byte) 3);
        Course course2 = new Course("TEST2","TEST2","description",(byte) 101,(byte) 3);
        Course course3 = new Course("TEST3","TEST3","description",(byte) 101,(byte) 3);
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);

        // assert that there are 3 courses withdrawal successfully
        assertEquals(gradeDAO.withdrawFromCourses(1, courses), 3);

        // get all the grades after the withdrawal
        grades = gradeDAO.getAllGrades();

        // assert that the tuples is successfully deleted from database
        assertFalse(grades.contains(g1));
        assertFalse(grades.contains(g2));
        assertFalse(grades.contains(g3));

    }

    @Test
    @DisplayName("Withdraw from multiple courses where all is disallowed because course_withdrawal_allowed is false")
    public void withdrawMultipleCoursesAllDisallowed(){

        // store the current state of withdrawal allowed ... because it will be changed then return again
        boolean currentState = Boolean.TRUE.equals(jdbcTemplate.queryForObject("""
                SELECT course_withdrawal_allowed
                FROM system_state
                """, Boolean.class));

        jdbcTemplate.update("""
                UPDATE system_state
                SET course_withdrawal_allowed = 0;
                """);

        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);

        // All the grades that inserted has passed is NULL ... it means that the student has the course in progress
        jdbcTemplate.update("""
                INSERT INTO grades (course_code, student_id, season, academic_year)
                VALUES
                  ('TEST1', 1, ?, ?),
                  ('TEST2', 1, ?, ?),
                  ('TEST3', 1, ?, ?);
                """, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear);

        List<Grade> grades = gradeDAO.getAllGrades();

        assert currentSeason != null;

        Grade g1 = new Grade("TEST1", 1, currentSeason.byteValue(), currentYear);
        Grade g2 = new Grade("TEST2", 1, currentSeason.byteValue(), currentYear);
        Grade g3 = new Grade("TEST3", 1, currentSeason.byteValue(), currentYear);

        boolean c1 = grades.contains(g1);
        boolean c2 = grades.contains(g2);
        boolean c3 = grades.contains(g3);

        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("TEST1","TEST1","description",(byte) 101,(byte) 3);
        Course course2 = new Course("TEST2","TEST2","description",(byte) 101,(byte) 3);
        Course course3 = new Course("TEST3","TEST3","description",(byte) 101,(byte) 3);
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);

        // assert that there are 3 courses withdrawal successfully
        assertEquals(gradeDAO.withdrawFromCourses(1, courses), 0);

        // get all the grades after the withdrawal
        grades = gradeDAO.getAllGrades();

        // assert that the tuples is not deleted from database
        assertTrue(grades.contains(g1));
        assertTrue(grades.contains(g2));
        assertTrue(grades.contains(g3));

        // restore the state
        jdbcTemplate.update("""
                UPDATE system_state
                SET course_withdrawal_allowed = ?;
                """, currentState);

        jdbcTemplate.update("DELETE FROM grades WHERE student_id = 1;");

    }


    @Test
    @DisplayName("Withdraw from multiple courses where some allowed and some not")
    public void withdrawMultipleCoursesMixed(){

        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);

        // passed attribute,
        // for the first is true -> will not be withdrawal
        // for the first is false -> will not be withdrawal
        // for the first is null -> will be withdrawal
        jdbcTemplate.update("""
                INSERT INTO grades (course_code, student_id, season, academic_year, passed)
                VALUES
                  ('TEST1', 1, ?, ?, 1),
                  ('TEST2', 1, ?, ?, 0),
                  ('TEST3', 1, ?, ?, NULL);
                """, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear);

        List<Grade> grades = gradeDAO.getAllGrades();

        assert currentSeason != null;

        Grade g1 = new Grade("TEST1", 1, currentSeason.byteValue(), currentYear);
        Grade g2 = new Grade("TEST2", 1, currentSeason.byteValue(), currentYear);
        Grade g3 = new Grade("TEST3", 1, currentSeason.byteValue(), currentYear);

        boolean c1 = grades.contains(g1);
        boolean c2 = grades.contains(g2);
        boolean c3 = grades.contains(g3);

        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("TEST1","TEST1","description",(byte) 101,(byte) 3);
        Course course2 = new Course("TEST2","TEST2","description",(byte) 101,(byte) 3);
        Course course3 = new Course("TEST3","TEST3","description",(byte) 101,(byte) 3);
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);

        // assert that only one is withdrawal
        assertEquals(gradeDAO.withdrawFromCourses(1, courses), 1);

        // get all the grades after the withdrawal
        grades = gradeDAO.getAllGrades();

        // assert the correction of deletion
        assertTrue(grades.contains(g1));
        assertTrue(grades.contains(g2));
        assertFalse(grades.contains(g3));

        jdbcTemplate.update("DELETE FROM grades WHERE student_id = 1;");

    }

}
