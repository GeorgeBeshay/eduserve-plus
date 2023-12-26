package edu.esp.controllers;

import edu.esp.system_entities.system_users.Student;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import edu.esp.services.StudentServices;
import edu.esp.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/esp-server/student-endpoint/")
@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.system_entities", "edu.esp.controllers"})
public class StudentEndPoint {

    JdbcTemplate jdbcTemplate;
    StudentServices studentServices;

    @Autowired
    public StudentEndPoint(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentServices = new StudentServices(this.jdbcTemplate);
    }

    @PostMapping("signIn")
    @ResponseBody
    public ResponseEntity<Boolean> signIn(@RequestBody Map<String, Object> requestMap) {
        Logger.logMsgFrom(this.getClass().getName(), "A Student has requested to sign in .. processing the request ..", -1);

        return (this.studentServices.signIn(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("signUp")
    @ResponseBody
    public ResponseEntity<Boolean> signUp(@RequestBody Map<String, Object> requestMap) {
        Logger.logMsgFrom(this.getClass().getName(), "A Student has requested to sign up .. processing the request ..", -1);

        return (this.studentServices.signUp(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("courseRegistrationSetup")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getCourseRegistrationSetup(@RequestBody Integer studentId) {
        // TODO use the getCourseRegistrationSetup method from the student service class
        return null;
    }

    /**
     * @param requestMap A map which includes student ID, taken credit hours, and list of course codes
     * @return A boolean which indicates success or failure
     */
    @PostMapping("saveRegisteredCourses")
    @ResponseBody
    public ResponseEntity<Boolean> saveRegisteredCourses(@RequestBody Map<String, Object> requestMap) {
        // TODO save the courses which the student has chosen
        return null;
    }

    /**
     * @param requestMap A map which includes the student ID along with the courses they wish to withdraw
     * @return A boolean which indicates success or failure
     */
    @PostMapping("withdrawCourses")
    @ResponseBody
    public ResponseEntity<Boolean> withdrawCourses(@RequestBody Map<String, Object> requestMap) {
        // TODO withdraw the given courses from the current semester of the given student id
        return null;
    }
}
