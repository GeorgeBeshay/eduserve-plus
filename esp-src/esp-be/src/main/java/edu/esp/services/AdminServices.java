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
     * Implements the business logic for registering a new admin.
     *
     * @param admin The admin object to be registered.
     * @return True if the registration is successful, otherwise false.
     */
    public boolean signUp(Admin admin) {

        if (dbFacade.createAdmin(admin)) {
            Logger.logMsgFrom(this.getClass().getName(), "New admin was successfully registered.", 0);
            return true;
        }

        Logger.logMsgFrom(this.getClass().getName(), "New Admin failed to be registered.", 1);
        return false;
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

    /**
     * get all the courses saved in the database.
     * It relies on the method provided by the dbFacade from the layer below it.
     * @return A list of Course objects (possibly an empty one).
     */
    public List<Course> getAllCourses() {

        List<Course> courses = dbFacade.getAllCourses();
        Logger.logMsgFrom(this.getClass().getName(), "get All the Courses successfully ..", 0);
        return courses;
    }

    /**
     * get all the un registered instructors saved in the database.
     * It relies on the method provided by the dbFacade from the layer below it.
     * @return A list of UnregisteredInstructor objects (possibly an empty one).
     */
    public List<UnregisteredInstructor> getAllUnregisteredInstructors() {

        List<UnregisteredInstructor> instructors = dbFacade.getAllUnregisteredInstructors();
        Logger.logMsgFrom(this.getClass().getName(), "get All the Unregistered Instructors successfully ..", 0);
        return instructors;
    }
}
