package edu.esp.controllers;

import edu.esp.database.DBFacade_Impl;
import edu.esp.system_entities.system_users.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"edu.esp.be","edu.esp.database","edu.esp.controllers","edu.esp.system_entities"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/esp-server/admin-controller/")
@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.controllers", "edu.esp.system_entities"})
public class AdminController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("signIn")
    @ResponseBody
    public ResponseEntity<Boolean> signIn(@RequestBody Admin admin) {
        System.out.println("Client: An admin has requested to sign in .. processing it ..");
        DBFacade_Impl dbFacade = new DBFacade_Impl(jdbcTemplate);
        try {
            Admin realAdmin = dbFacade.loadAdmin(admin.getAdminId());
            if(realAdmin != null){
                if(admin.getAdminPwHash() == realAdmin.getAdminPwHash()){
                    System.out.println("Admin was successfully authenticated.");
                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
                } else {
                    System.out.println("Admin was not authenticated.");
                    return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            System.out.println("Admin was not authenticated.");
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("signUp")
    @ResponseBody
    public ResponseEntity<Boolean> signUp(@RequestBody Admin admin) {
        System.out.println("Client: An admin has requested to register .. processing it ..");
        DBFacade_Impl dbFacade = new DBFacade_Impl(jdbcTemplate);
        if(dbFacade.createAdmin(admin)){
            System.out.println("Admin was successfully registered.");
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        System.out.println("Admin failed to be registered.");
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

}
