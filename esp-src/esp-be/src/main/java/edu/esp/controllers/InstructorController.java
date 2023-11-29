package edu.esp.controllers;

import edu.esp.system_entities.system_users.Instructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/esp-server/instructor-controller/")
public class InstructorController {

    @PostMapping("/signIn")
    @ResponseBody
    public Boolean signIn(@RequestBody Instructor instructor) {
        //check if the username and password are correct
        //and send message to the front
        System.out.println(instructor.toString());
        return true;
    }

    @PostMapping("/signUp")
    @ResponseBody
    public Boolean signUp(@RequestBody Instructor instructor) {
        //check if the username and password are correct
        //update the password in database
        //and send message to the front
        System.out.println(instructor.toString());
        return true;
    }
}
