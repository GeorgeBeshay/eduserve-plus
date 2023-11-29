package edu.esp.controllers;

import edu.esp.system_entities.system_users.Admin;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/esp-server/admin-controller/")

public class AdminController {

    @PostMapping("/signIn")
    @ResponseBody
    public Boolean signIn(@RequestBody Admin admin) {
        //check if the username and password are correct
        //and send message to the front
        System.out.println(admin.toString());
        return true;
    }

    @PostMapping("/signUp")
    @ResponseBody
    public Boolean signUp(@RequestBody Admin admin) {
        //check if the username and password are correct
        //update the password in database
        //and send message to the front
        System.out.println(admin.toString());
        return true;
    }

}
