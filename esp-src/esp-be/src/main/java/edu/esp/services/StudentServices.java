package edu.esp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_users.Student;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
}