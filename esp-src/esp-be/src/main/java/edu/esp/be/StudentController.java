package edu.esp.be;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/student")
public class StudentController {

    @PostMapping("/login")
    @ResponseBody
    public Boolean login(@RequestBody Student student) throws IOException, ParseException {
        //check if the username and password are correct
        //and send message to the front
        System.out.println(student.toString());
        return true;
    }

    @PostMapping("/signup")
    @ResponseBody
    public Boolean signup(@RequestBody Student student) throws IOException, ParseException {
        //check if the username and password are correct
        //update the password in data base
        //and send message to the front
        System.out.println(student.toString());
        return true;
    }
}
