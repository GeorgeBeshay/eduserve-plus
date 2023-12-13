package edu.esp.controllers;

import edu.esp.system_entities.system_users.Student;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/esp-server/student-controller/")
@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.system_entities", "edu.esp.controllers"})
public class StudentController {

    @PostMapping("/signIn")
    @ResponseBody
    public Boolean signIn(@RequestBody Student student) {
        //check if the username and password are correct
        //and send message to the front
        System.out.println(student.toString());
        return true;
    }

    @PostMapping("/signUp")
    @ResponseBody
    public Boolean signUp(@RequestBody Student student) {
        //check if the username and password are correct
        //update the password in database
        //and send message to the front
        System.out.println(student.toString());
        return true;
    }
}
