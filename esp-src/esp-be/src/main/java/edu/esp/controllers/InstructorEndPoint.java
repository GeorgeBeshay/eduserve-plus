package edu.esp.controllers;

import edu.esp.services.InstructorServices;
import edu.esp.system_entities.system_users.Instructor;
//import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.utilities.Hasher;
import edu.esp.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/esp-server/instructor-endpoint/")
@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.controllers", "edu.esp.system_entities","edu.esp.services"})
public class InstructorEndPoint {

    JdbcTemplate jdbcTemplate;
    InstructorServices instructorServices ;
    @Autowired
    public InstructorEndPoint(JdbcTemplate jdbcTemplate, InstructorServices instructorServices) {
        this.jdbcTemplate = jdbcTemplate;
        this.instructorServices = instructorServices;
    }

    @PostMapping("signIn")
    @ResponseBody
    public ResponseEntity<Boolean> signIn(@RequestBody Map<String, Object> requestMap) {
        Logger.logMsgFrom(this.getClass().getName(), "An Instructor has requested to sign in .. processing the request ..", -1);

        return (this.instructorServices.signIn(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("signUp")
    @ResponseBody
    public ResponseEntity<Boolean> signUp(@RequestBody Map<String,Object> requestMap){
        Logger.logMsgFrom(this.getClass().getName(), "Client side requested to register a new instructor .. processing the request ..", -1);

        return (this.instructorServices.signUp(requestMap))
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

    }


}

