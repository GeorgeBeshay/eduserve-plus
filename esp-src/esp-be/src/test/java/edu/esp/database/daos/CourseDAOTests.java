package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.Arrays;
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
        jdbcTemplate.update("""
                INSERT INTO department (dpt_id, dpt_name)
                VALUES
                  (101, 'Test DPT 1'),
                  (102, 'Test DPT 2'),
                  (103, 'Test DPT 3');
                """);
        this.courseDAO = new CourseDAO( jdbcTemplate );
    }

    @AfterAll
    public void cleanUp() {
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id IN (101,102,103);");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course with repeated id")
    public void addNewCourseRepeatedId() {

        Course course = new Course("TEST1","programming1",
                "bla bla", (byte) 101,(byte) 3);

        Course repeatedCourse = new Course("TEST1","paradigms",
                "bla bla", (byte) 101,(byte) 3);

        assertTrue( courseDAO.addNewCourse(course) );
        assertFalse( courseDAO.addNewCourse(repeatedCourse) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST1';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite size is 0")
    public void addNewCourseWithPrerequisitesSize0(){

        Course course = new Course("TEST2","programming1",
                "bla bla", (byte) 101,(byte) 3);

        List<String> preReq = new ArrayList<>();
        course.setPrerequisite(preReq);

        assertTrue(courseDAO.addNewCourse(course));

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST2';");
    }

    @Test
    @DisplayName ("Course DAO - try to add new course without prerequisite (prerequisite is null")
    public void addNewCourseWithNullPrerequisites(){

        Course course = new Course("TEST3","programming1",
                "bla bla", (byte) 101,(byte) 3);
        course.setPrerequisite( null );

        assertTrue( courseDAO.addNewCourse(course) );

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST3';");
    }

    @Test
    @DisplayName( "Course DAO - try to add new course with valid prerequisite" )
    public void addNewCourseWithValidPrerequisites(){

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 101,(byte) 3);
        Course pre2 = new Course("PRE2","math2","lkdmf",(byte) 101,(byte) 3);
        courseDAO.addNewCourse(pre1);
        courseDAO.addNewCourse(pre2);


        Course course = new Course("TEST4","programming1",
                "bla bla", (byte) 101,(byte) 3);

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

        Course pre1 = new Course("PRE1","math1","lkdmf",(byte) 101,(byte) 3);

        courseDAO.addNewCourse(pre1);

        Course course = new Course("TEST5","programming1",
                "bla bla", (byte) 101,(byte) 3);

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
        byte offeringDpt = 104;
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
        byte offeringDpt = 104;
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
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", offeringDpt);
    }

    @Test
    @DisplayName("Passing an offeringDpt that offers multiple, specifically 6, courses.")
    public void findByOfferingDptMultipleMatches() {
        // Arrange
        String[] courseCodes = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5", "TEST6"};
        byte offeringDpt = 104;

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
        jdbcTemplate.update("DELETE FROM department WHERE dpt_id = ?", offeringDpt);
    }
                            
    @Test
    @DisplayName("Testing checking whether registration is open or not")
    public void registrationIsOpenOrNot() {
        Integer originalValue = jdbcTemplate.queryForObject("SELECT course_registration_allowed FROM system_state", Integer.class);
        // Arrange
        jdbcTemplate.update("UPDATE system_state SET course_registration_allowed = 1 WHERE handle = 0");
        // Act & Assert
        assertTrue(courseDAO.courseRegistrationOpen());
        // Arrange
        jdbcTemplate.update("UPDATE system_state SET course_registration_allowed = 0 WHERE handle = 0");
        // Act & Assert
        assertFalse(courseDAO.courseRegistrationOpen());
        // Clean
        jdbcTemplate.update("UPDATE system_state SET course_registration_allowed = %d WHERE handle = 0".formatted(originalValue));
    }

    @Test
    @DisplayName("Testing all scenarios of available courses for registration")
    public void getAvailableRegistrationCourses() {
        // Arrange
        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);
        String sqlSetup = """
                INSERT INTO instructor
                VALUES
                    (1, 135790, 101, 'Prof. Anderson', '555-4321', 'prof.anderson@example.com', 'Monday 10am-12pm, Wednesday 2pm-4pm'),
                    (3, 987123, 103, 'Prof. Williams', '555-9876', 'prof.williams@example.com', 'Friday 3pm-5pm, Saturday 10am-12pm');
                
                INSERT INTO student
                VALUES (1, 456789, 101, 2, 3.5, 'John Doe', '12345678901234', '2000-01-01', '123 Main St', '555-1234', '123-4567', 1, 'john.doe@example.com');
                
                INSERT INTO semester
                VALUES (1, '2022', '2022-2-2', '2022-6-6');
                
                INSERT INTO course (course_code, course_name, offering_dpt, credit_hrs)
                VALUES
                    ('T0', 'Test Course 0', 101, 2), -- Won't show (passed)
                    ('T1', 'Test Course 1', 101, 3), -- Won't show (passed)
                    ('T2', 'Test Course 2', 101, 2), -- Will show (not taken)
                    ('T3', 'Test Course 3', 101, 3), -- Will show (not passed)
                    ('T4', 'Test Course 4', 103, 2), -- Won't show (different department)
                    ('T5', 'Test Course 5', 101, 3), -- Won't show (not offered)
                    ('T6', 'Test Course 6', 101, 3), -- Won't show (already registered this semester)
                    ('T7', 'Test Course 7', 101, 2); -- Won't show (one out of 2 prerequisites completed)
                
                INSERT INTO course_prereq
                VALUES ('T2', 'T0'), ('T2', 'T1'), ('T3', 'T0'), ('T3', 'T1'), ('T7', 'T3'), ('T7', 'T1');
                
                INSERT INTO grades
                VALUES
                    ('T0', 1, 1, '2022', 30, 50, 80, 1), ('T1', 1, 1, '2022', 25, 48, 80, 1),
                    ('T3', 1, 1, '2022', 2, 12, 50, 0), ('T6', 1, %d, %s, null, null, null, 0);
                    
                INSERT INTO instructs
                VALUES
                    (1, 'T0', %d, '%s'), (1, 'T1', %d, '%s'), (1, 'T2', %d, '%s'), (1, 'T3', %d, '%s'),
                    (3, 'T4', %d, '%s'), (1, 'T6', %d, '%s'), (1, 'T7', %d, '%s');
                """.formatted(
                        currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear,
                        currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear
                );
        jdbcTemplate.batchUpdate(sqlSetup);

        // Act
        List<Course> availableCourses = courseDAO.getAvailableCourses(1);

        // Assert
        assertEquals(2, availableCourses.size());
        boolean contains2 = false, contains3 = false;
        for (Course course : availableCourses) {
            if (course.getCourseCode().equals("T2")) {
                contains2 = true;
                assertEquals("Test Course 2", course.getCourseName());
                assertEquals((byte)101, course.getOfferingDpt());
                assertEquals((byte)2, course.getCreditHrs());
            } else if (course.getCourseCode().equals("T3")) {
                contains3 = true;
                assertEquals("Test Course 3", course.getCourseName());
                assertEquals((byte)101, course.getOfferingDpt());
                assertEquals((byte)3, course.getCreditHrs());
            }
        }
        assertTrue(contains2 && contains3);

        // Clean
        String sqlFinish = """
                DELETE FROM instructs WHERE instructor_id IN (1,3);
                DELETE FROM grades WHERE student_id = 1;
                DELETE FROM course WHERE course_code IN ('T0','T1','T2','T3','T4','T5','T6','T7');
                DELETE FROM semester WHERE season = 1 AND academic_year = '2022';
                DELETE FROM student WHERE student_id = 1;
                DELETE FROM instructor WHERE instructor_id IN (1,3);
                """;
        jdbcTemplate.batchUpdate(sqlFinish);
    }

    @Test
    @DisplayName("get all courses where there exist courses in the database.")
    public void GetAllCoursesExist() {

        String[] courseCodes = {"TEST1", "TEST2"};
        jdbcTemplate.update("INSERT INTO course (course_code, offering_dpt) VALUES ('TEST1', 101)");
        jdbcTemplate.update("INSERT INTO course (course_code, offering_dpt) VALUES ('TEST2', 101)");
        List<Course> courses = courseDAO.getAllCourses();

        assertTrue(courses.size() >= 2);

        List<String> codes = new ArrayList<>();

        for (Course course: courses){
            codes.add(course.getCourseCode());
        }

        assertTrue(codes.contains("TEST1"));
        assertTrue(codes.contains("TEST2"));

        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = ?", 101);

    }

    @Test
    @Disabled
    @DisplayName("get all courses where there don't exist courses in the database.")
    public void GetAllCoursesNotExist() {

        List<Course> courses = courseDAO.getAllCourses();

        assertNotNull(courses);
        assertTrue(courses.isEmpty());

    }


    @Test
    @DisplayName("get available course for withdrawal where student_id is valid and there existed courses for withdrawal")
    public void getCoursesToWithdrawExists() {

        // setup
        jdbcTemplate.update("""
                INSERT INTO student (student_id, dpt_id, ssn)
                VALUES
                  (1, 101, 1);
                """);

        jdbcTemplate.update("""
                INSERT INTO course (course_code, course_name, course_description, offering_dpt, credit_hrs)
                VALUES
                  ('TEST1', 'TEST1', 'description', 101, 3),
                  ('TEST2', 'TEST2', 'description', 101, 3),
                  ('TEST3', 'TEST3', 'description', 101, 3),
                  ('TEST4', 'TEST4', 'description', 101, 3);
                """);

        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);

        jdbcTemplate.update("""
                INSERT INTO grades (course_code, student_id, season, academic_year, passed)
                VALUES
                  ('TEST1', 1, ?, ?, 1),
                  ('TEST2', 1, ?, ?, 0),
                  ('TEST3', 1, ?, ?, NULL),
                  ('TEST4', 1, ?, ?, NULL);
                """, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear);
        // end of set up

        // the first course is already passed -> will not be returned
        // the second course is already failed -> will not be returned
        // the third course is in progress -> will be returned
        // the fourth course is in progress -> will be returned
        List<Course> courses = courseDAO.getAvailableWithdrawCourses(1);

        assertEquals(2, courses.size());

        List<String> courseCodes = new ArrayList<>();

        for (Course course: courses){
            courseCodes.add(course.getCourseCode());
        }


        assertTrue(courseCodes.contains("TEST3"));
        assertTrue(courseCodes.contains("TEST4"));

        assertFalse(courseCodes.contains("TEST1"));
        assertFalse(courseCodes.contains("TEST2"));


        // delete the set-up
        jdbcTemplate.update("DELETE FROM grades WHERE student_id = 1;");
        jdbcTemplate.update("DELETE FROM student WHERE dpt_id = 101");
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = 101");


    }


    @Test
    @DisplayName("get available course for withdrawal where student_id is not valid or there doesn't existed courses for withdrawal")
    public void getCoursesToWithdrawNotExists() {

        // setup
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
                  ('TEST3', 'TEST3', 'description', 101, 3),
                  ('TEST4', 'TEST4', 'description', 101, 3);
                """);

        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);

        jdbcTemplate.update("""
                INSERT INTO grades (course_code, student_id, season, academic_year, passed)
                VALUES
                  ('TEST1', 1, ?, ?, 1),
                  ('TEST2', 1, ?, ?, 0),
                  ('TEST3', 1, ?, ?, 1),
                  ('TEST4', 1, ?, ?, 0);
                """, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear, currentSeason, currentYear);
        // end of set up

        // all the grades in database for student 1
        // so student 2 has no courses to withdraw
        List<Course> courses = courseDAO.getAvailableWithdrawCourses(2);

        assertNotNull( courses );
        assertTrue( courses.isEmpty() );

        // all the grades for student 1 is passed for failed
        // so student 1 has no courses to withdraw
        courses = courseDAO.getAvailableWithdrawCourses(1);

        assertNotNull( courses );
        assertTrue( courses.isEmpty() );


        // delete the set-up
        jdbcTemplate.update("DELETE FROM grades WHERE student_id = 1;");
        jdbcTemplate.update("DELETE FROM student WHERE dpt_id = 101");
        jdbcTemplate.update("DELETE FROM course WHERE offering_dpt = 101");


    }

}
