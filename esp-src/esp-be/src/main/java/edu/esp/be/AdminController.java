package edu.esp.be;



import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/admin")

public class AdminController {

    @PostMapping("/login")
    @ResponseBody
    public Boolean login(@RequestBody Admin admin) throws IOException, ParseException {
        //check if the username and password are correct
        //and send message to the front
        System.out.println(admin.toString());
        return true;
    }

    @PostMapping("/signup")
    @ResponseBody
    public Boolean signup(@RequestBody Admin admin) throws IOException, ParseException {
        //check if the username and password are correct
        //update the password in data base
        //and send message to the front
        System.out.println(admin.toString());
        return true;
    }

}
