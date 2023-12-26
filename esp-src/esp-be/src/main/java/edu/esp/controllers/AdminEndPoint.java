package edu.esp.controllers;

import edu.esp.services.AdminServices;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.system_entities.system_users.UnregisteredStudent;
import edu.esp.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/esp-server/admin-endpoint/")
@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.controllers", "edu.esp.system_entities"})
public class AdminEndPoint {

    JdbcTemplate jdbcTemplate;
    AdminServices adminServices;

    @Autowired
    public AdminEndPoint(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.adminServices = new AdminServices(this.jdbcTemplate);
    }

    @PostMapping("signIn")
    @ResponseBody
    public ResponseEntity<Boolean> signIn (@RequestBody Map<String, Object> requestMap) {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to sign in .. processing the request ..", -1);

        return (this.adminServices.signIn(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addNewAdmin")
    @ResponseBody
    public ResponseEntity<Boolean> addNewAdmin (@RequestBody Map<String, Object> requestMap) {
        Logger.logMsgFrom(this.getClass().getName(), "Client side requested to add a new admin .. processing the request ..", -1);

        return (this.adminServices.addNewAdmin(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("CreateUnregisteredInstructor")
    @ResponseBody
    public ResponseEntity<Boolean> registerInstructor(@RequestBody UnregisteredInstructor unregisteredInstructor){
        Logger.logMsgFrom(this.getClass().getName(), "Client side requested to add a new unregistered instructor .. processing the request ..", -1);
        return (this.adminServices.addNewUnregisteredInstructor(unregisteredInstructor))
                ? new ResponseEntity<>(true,HttpStatus.OK)
                : new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addCourse")
    @ResponseBody
    public ResponseEntity<Boolean> addCourse (@RequestBody Course newCourse) {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to add new course .. processing the request ..", -1);

        return (this.adminServices.addNewCourse(newCourse))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addUnregisteredStudents")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addUnregisteredStudents (@RequestParam("unregisteredStudents") MultipartFile unregisteredStudents) {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to add unregistered students .. processing the request ..", -1);

        Map<String, Object> resultOfAddingStudents = this.adminServices.addUnregisteredStudents(unregisteredStudents);

        return (!resultOfAddingStudents.get("studentsSuccessfullyAdded").equals(0))
                ? new ResponseEntity<>(resultOfAddingStudents, HttpStatus.OK)
                : new ResponseEntity<>(resultOfAddingStudents, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("getAllCourses")
    @ResponseBody
    public ResponseEntity<List<Course>> getAllCourses () {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to get all courses .. processing the request ..", -1);
        return new ResponseEntity<>(this.adminServices.getAllCourses(), HttpStatus.OK);

    }

    @PostMapping("getAllUnregisteredInstructors")
    @ResponseBody
    public ResponseEntity<List<UnregisteredInstructor>> getAllUnregisteredInstructors () {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to get all Unregistered Instructors .. processing the request ..", -1);
        return new ResponseEntity<>(this.adminServices.getAllUnregisteredInstructors(), HttpStatus.OK);

    }

    @PostMapping("getAllUnregisteredStudents")
    @ResponseBody
    public ResponseEntity<List<UnregisteredStudent>> getAllUnregisteredStudents () {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to get all Unregistered Students .. processing the request ..", -1);
        return new ResponseEntity<>(this.adminServices.getAllUnregisteredStudents(), HttpStatus.OK);

    }


}
