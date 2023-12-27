package edu.esp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Student;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Map<String, Object> getCourseRegistrationSetup (int studentId) {
        // TODO use the getAvailableCourses and getAvailableCreditHours methods to return a map
        return null;
    }

    private List<Course> getAvailableCourses(int studentId) {
        // TODO get the list of available courses for student registration
        return null;
    }

    private int getAvailableCreditHours(int studentId) {
        // TODO maybe create a new DAO for the "attends" table
        // TODO get student available credit hours
        return 0;
    }

    public int withdrawCourses(Map<String, Object> requestMap){
        int studentId = (int) requestMap.get("studentId");
        List<Course> courses = (new ObjectMapper()).convertValue(requestMap.get("courses"), new TypeReference<List<Course>>() {});
        int result = dbFacade.withdrawFromCourses(studentId, courses);
        Logger.logMsgFrom(this.getClass().getName(), "withdrawed from: %d courses".formatted(result), 0);
        return result;
    }

    public List<Course> getAvailableWithdrawCourses(int studentId){
        List<Course> courses = dbFacade.getAvailableWithdrawCourses(studentId);
        if (courses != null){
            Logger.logMsgFrom(this.getClass().getName(), "get available withdrawal courses successfully", 0);
            return courses;
        }
        else{
            Logger.logMsgFrom(this.getClass().getName(), "there isn't available courses for withdrawal", 0);
            return new ArrayList<>();
        }
    }
}
