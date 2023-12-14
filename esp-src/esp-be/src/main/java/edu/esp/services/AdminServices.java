package edu.esp.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class represents the "Brain" of all operations related to the admin entity, as it implements all the business logic
 * related to the admins operations by relying on the services provided by the layer underneath it and at the same time
 * provides the layer on top of it (Endpoints layer) by the essential functionalities and services its needs.
 */
@Service
public class AdminServices {
    private final JdbcTemplate jdbcTemplate;
    private final DBFacadeImp dbFacade;       // TODO: Instantiate the object as of type DBFacadeIF instead.

    public AdminServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbFacade = new DBFacadeImp(this.jdbcTemplate);
    }

    /**
     * Implements the business logic for adding a new admin to the system. It relies on the methods provided by the
     * dbFacade object from the layer below it, and converts the admin password to the hashed version of it.
     * @param requestMap A map that contains the "admin" field which essentially contains all the admin real data
     * except for the password, which is sent in a dedicated field "adminPw", so that it can be further hashed
     * and then stored in the DB. We can't send the 'adminPw' field in the admin object itself, as the object stores
     * an attribute of type int and not a string.
     * @return a boolean flag that indicates the success or failure of the process.
     */
    public boolean addNewAdmin(Map<String,Object> requestMap) {

        try {
            // Check that the map contains the needed fields.
            assert requestMap != null : "Request Map was found to be null!";
            assert requestMap.containsKey("admin") : "Request Map does not contain the 'admin' field!";
            assert requestMap.containsKey("adminPw") : "Request Map does not contain the 'adminPw' field!";

            // Prepare the real admin object, that to be stored in the DB.
            Admin adminToBeAdded = (new ObjectMapper()).convertValue(requestMap.get("admin"), Admin.class);
            int hashedPassword = Hasher.hash((String)requestMap.get("adminPw"));

            // Check for special cases
            assert adminToBeAdded != null : "Admin object sent was null.";
            assert hashedPassword != -1 : "Admin password couldn't be hashed.";

            adminToBeAdded.setAdminPwHash(hashedPassword);
            assert dbFacade.createAdmin(adminToBeAdded) : "New admin wasn't created.";

            Logger.logMsgFrom(this.getClass().getName(), "New admin was successfully created.", 0);
            return true;

        } catch (Exception | Error e) {
            Logger.logMsgFrom(this.getClass().getName(), e.getMessage(), 1);
            return false;

        }

    }

    /**
     * Implements the business logic for authenticating an admin sign in.
     *
     * @param requestMap contains the admin object (without the password) to be signed in and the password before hashing
     * @return True if the authentication is successful, otherwise false.
     */
    public boolean signIn(Map<String, Object> requestMap) {
        // Extract Admin and password converted to hashed value
        Admin admin = (new ObjectMapper()).convertValue(requestMap.get("admin"), Admin.class);
        int hashedPassword = Hasher.hash((String) requestMap.get("password"));

        // Avoid null objects send from the client side.
        if (admin == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin object sent was null.", 1);
            return false;
        }
        if (hashedPassword == -1) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin password can't be hashed.", 1);
            return false;
        }
        admin.setAdminPwHash(hashedPassword);

        // Fetch the object with the same id, accessing it using admin.get() is safe.
        Admin realAdminObject = this.dbFacade.loadAdmin(admin.getAdminId());
        if (realAdminObject == null) {  // if not found, then the id given does not exist.
            Logger.logMsgFrom(this.getClass().getName(), "Admin object sent was not found in the DB.", 1);
            return false;
        }

        // Up to this point, the real object and sent object are not null, and have the same id.
        if ((admin.getAdminPwHash() == realAdminObject.getAdminPwHash())) {
            Logger.logMsgFrom(this.getClass().getName(), "Admin has been authenticated.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "Admin password is incorrect.", 1);
        return false;
    }

    public boolean addNewUnregisteredInstructor(UnregisteredInstructor unregisteredInstructor) {
        if (this.dbFacade.addNewUnregisteredInstructor(unregisteredInstructor)) {
            Logger.logMsgFrom(this.getClass().getName(), "New unregistered instructor was successfully added to the system.", 0);
            return true;
        }
        Logger.logMsgFrom(this.getClass().getName(), "New Unregistered Instructor failed to be added to the system.", 1);
        return false;

    }

    /**
     * Implements the business logic for adding new course to DB.
     *
     * @param newCourse the course object of the new course having the prerequisite List.
     * @return True if the course is added successful, otherwise False.
     */
    public boolean addNewCourse ( Course newCourse ){
        // call the DB to add that course
        if ( dbFacade.addNewCourse( newCourse ) ){
            Logger.logMsgFrom( this.getClass().getName(),"the course added successfully",0 );
            return true;
        }
        else {
            Logger.logMsgFrom( this.getClass().getName(),"the course didn't be added",1 );
            return false;
        }
    }

    /**
     * Implements the business logic of fetching courses that are being offered by a specific department.
     * It relies on the method provided by the dbFacade from the layer below it.
     * @param offeringDpt The id of the department, that we are looking for the courses offered by.
     * @return A list of Course objects (possibly an empty one) that were found to be offered by the mentioned department.
     * NOTE: An empty list may be returned in 2 cases:
     * 1. The department offers no courses.
     * 2. An invalid ID or a SQL query execution error had occurred.
     */
    public List<Course> fetchCoursesOfferedByDpt(byte offeringDpt) {
        // if null, then either the offeringDpt is negative, or a SQL query execution error had occurred.
        List<Course> matchingCourses = dbFacade.fetchCoursesOfferedByDpt(offeringDpt);

        if(matchingCourses == null) {
            Logger.logMsgFrom(this.getClass().getName(), "Something went wrong during courses fetching process .. returning an empty list.", 1);
            return new ArrayList<>();

        } else {
            Logger.logMsgFrom(this.getClass().getName(), "Matching courses were successfully found ..", 0);
            return matchingCourses;

        }
    }
}
