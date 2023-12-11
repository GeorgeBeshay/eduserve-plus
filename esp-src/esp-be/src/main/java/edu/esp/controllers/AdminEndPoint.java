package edu.esp.controllers;

import edu.esp.services.AdminServices;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("signUp")
    @ResponseBody
    public ResponseEntity<Boolean> signUp (@RequestBody Admin admin) {

        Logger.logMsgFrom(this.getClass().getName(), "Client side requested to register a new admin .. processing the request ..", -1);

        return (this.adminServices.signUp(admin))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("addCourse")
    @ResponseBody
    public ResponseEntity<Boolean> addCourse (@RequestBody Course newCourse) {

        Logger.logMsgFrom(this.getClass().getName(), "An admin has requested to add new course .. processing the request ..", -1);

        return (this.adminServices.addNewCourse(newCourse))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

}
