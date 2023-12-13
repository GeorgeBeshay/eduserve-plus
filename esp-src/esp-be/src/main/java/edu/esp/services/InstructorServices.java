package edu.esp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class InstructorServices {
    private final JdbcTemplate jdbcTemplate;
    private final DBFacadeImp dbFacade;       // TODO: Instantiate the object as of type DBFacadeIF instead.

    public InstructorServices(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.dbFacade = new DBFacadeImp(this.jdbcTemplate);
    }
    public boolean signUp (Map<String,Object> requestMap) {
        Instructor instructor = (new ObjectMapper()).convertValue(requestMap.get("instructor"),Instructor.class);
        int hashedPassword = Hasher.hash((String)requestMap.get("newPassword"));
        int hashedOTPPassword = Hasher.hash((String) requestMap.get("OTPPassword"));
        if (instructor == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Instructor object sent was null.", 1);
            return false;
        }
        // -1 returned from hash function in case of there is problem in hashing
        if (hashedPassword == -1 || hashedOTPPassword == -1) {
            Logger.logMsgFrom(this.getClass().getName(), "Instructor password can't be hashed.", 1);
            return false;
        }
        instructor.setInstructorPwHash(hashedPassword);

        if( dbFacade.signUpInstructor(instructor.getInstructorId(),hashedOTPPassword,instructor) ){
            Logger.logMsgFrom(this.getClass().getName(), "New instructor was successfully registered.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "New instructor failed to be registered.", 1);
        return false;
    }
    public boolean signIn (Map<String, Object> requestMap) {
        // Extract Admin and password converted to hashed value
        Instructor instructor = (new ObjectMapper()).convertValue(requestMap.get("instructor"), Instructor.class);
        int hashedPassword = Hasher.hash((String) requestMap.get("password"));

        // Avoid null objects send from the client side.
        if (instructor == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Instructor object sent was null.", 1);
            return false;
        }
        if (hashedPassword == -1) {
            Logger.logMsgFrom(this.getClass().getName(), "Instructor password can't be hashed.", 1);
            return false;
        }
        instructor.setInstructorPwHash(hashedPassword);

        // Fetch the object with the same id, accessing it using admin.get() is safe.
        Instructor realInstructorObject = this.dbFacade.loadInstructorData(instructor.getInstructorId());
        if (realInstructorObject == null) {  // if not found, then the id given does not exist.
            Logger.logMsgFrom(this.getClass().getName(), "Instructor object sent was not found in the DB.", 1);
            return false;
        }

        // Up to this point, the real object and sent object are not null, and have the same id.
        if ((instructor.getInstructorPwHash() == realInstructorObject.getInstructorPwHash())){
            Logger.logMsgFrom(this.getClass().getName(), "Instructor has been authenticated.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Instructor password is incorrect.", 1);
        return false;
    }
}
