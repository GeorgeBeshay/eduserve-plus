package edu.esp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Student;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is for all the services of the student
 */
@Service
public class StudentServices {
    private final JdbcTemplate jdbcTemplate;
    private final DBFacadeImp dbFacade;

    public StudentServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbFacade = new DBFacadeImp(this.jdbcTemplate);
    }

    /**
     * Check for student sign in
     * First it check that the student of this id present in DB
     * Then it checks if the hashed password of student is the same as the hashed password in DB
     * @param requestMap conatins the student object (without the password) to be signed in and the password before hashing
     * @return True if the sign in is successful, otherwise false
     */
    public boolean signIn (Map<String, Object> requestMap) {
        // Extract the student object from the map and the password then convert it to be hashed
        Student student = (new ObjectMapper()).convertValue(requestMap.get("student"), Student.class);
        int hashedPassword = Hasher.hash((String) requestMap.get("password"));

        if (student == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Student object sent was null.", 1);
            return false;
        }
        // -1 returned from hash function in case of there is problem in hashing
        if (hashedPassword == -1) {
            Logger.logMsgFrom(this.getClass().getName(), "Student password can't be hashed.", 1);
            return false;
        }
        student.setStudentPwHash(hashedPassword);

        Student studentFromDB = this.dbFacade.loadStudentData(student.getStudentId());
        if (studentFromDB == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Student object sent was not found in the DB.", 1);
            return false;
        }

        if(student.getStudentPwHash() == studentFromDB.getStudentPwHash()) {
            Logger.logMsgFrom(this.getClass().getName(), "Student has been authenticated.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Student password is incorrect.", 1);
        return false;
    }

    public Boolean signUp(Map<String, Object> requestMap) {
        // Extract the student object from the map and the password then convert it to be hashed
        Student student = (new ObjectMapper()).convertValue(requestMap.get("student"), Student.class);
        int hashedPassword = Hasher.hash((String) requestMap.get("password"));
        int hashedOTPPassword = Hasher.hash((String) requestMap.get("OTPPassword"));

        if (student == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Student object sent was null.", 1);
            return false;
        }
        // -1 returned from hash function in case of there is problem in hashing
        if (hashedPassword == -1 || hashedOTPPassword == -1) {
            Logger.logMsgFrom(this.getClass().getName(), "Student password can't be hashed.", 1);
            return false;
        }
        student.setStudentPwHash(hashedPassword);

        if (dbFacade.signUpStudent(student.getStudentId(), hashedOTPPassword, student)) {
            Logger.logMsgFrom(this.getClass().getName(), "New Student was successfully registered.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "New Student failed to be registered.", 1);
        return false;
    }

    /**
     * Method returns one of the following:<br>
     * 1. A map containing all the needed information for the registration process.
     * 2. or a null object to indicate that courses registration is currently not available.
     * @param studentId: The id of the student who is requesting to register new courses.
     * @return courseRegistrationSetupMap or null
     * @apiNote <br>
     * 1. Null object indicates that the registration is not available. <br>
     * 2. Null availableCourses list indicate that something had went wrong when executing the query on the DB. <br>
     */
    public Map<String, Object> getCourseRegistrationSetup (int studentId) {
        if(!dbFacade.courseRegistrationOpen()) {
            Logger.logMsgFrom(this.getClass().getName(),
                    "Sorry, Courses Registration is currently not available.", 1);
            return null;
        }

        Map<String, Object> courseRegistrationSetupMap = new HashMap<>();
        courseRegistrationSetupMap.put("availableCourses", getAvailableCourses(studentId));
        courseRegistrationSetupMap.put("availableCreditHours", getAvailableCreditHours(studentId));

        return courseRegistrationSetupMap;
    }

    private List<Course> getAvailableCourses(int studentId) {

        List<Course> availableCourses = dbFacade.fetchAvailableRegistrationCourses(studentId);
        if(availableCourses == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Something had went wrong when fetching the available courses .. ", 1);
        }

        return availableCourses;
    }

    private int getAvailableCreditHours(int studentId) {
        // TODO maybe create a new DAO for the "attends" table
        // TODO get student available credit hours
        // currently, we are assuming a static # of credit hours = 21.
        return 21;
    }

    /**
     * Implements the procedure for registering a set of courses, given the registrationMap data structure.
     * @param registrationMap A Map containing: <br>
     * 1. The id of the student performing the registration. <br>
     * 2. The total # of credit hours of the selected courses. <br>
     * 3. A list of the selected courses codes. <br>
     * @return The number of successful registered courses from the ones selected.
     */
    public int registerCourses(Map<String, Object> registrationMap) {
        // guard check
        assert registrationMap != null : "registrationMap was found to be null.";
        assert registrationMap.containsKey("studentId") : "registrationMap had no studentId <K, V> entry.";
        assert registrationMap.containsKey("selectedCourses") : "registrationMap had no selectedCourses <K,V> entry.";
        assert registrationMap.containsKey("totalNumberOfHours") : "registrationMap had no totalNumberOfHours <K, V> entry.";

        @SuppressWarnings("unchecked")
        List<String> selectedCoursesCodes = (List<String>) registrationMap.get("selectedCourses");
        int studentId = (int) registrationMap.get("studentId");
        int totalNumberOfHours = (int) registrationMap.get("totalNumberOfHours");

        int successfullyRegisteredCoursesCount = dbFacade.registerCourses(studentId, selectedCoursesCodes);
        Logger.logMsgFrom(this.getClass().getName(), "Number of successfully registered courses = "
                + successfullyRegisteredCoursesCount, -1);

        // TODO update the attends method using the `totalNumberOfHours` variable.

        return successfullyRegisteredCoursesCount;
    }

}
