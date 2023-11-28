package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//@Service
public class StudentDAO {

    private JdbcTemplate jdbcTemplate;

//    @Autowired
    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Student getStudentById(int studentId) {
        String query = "SELECT * FROM student WHERE id = " + Integer.toString(studentId);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
        return null;
    }

    public void hello(){
        System.out.println("Hellooo");
    }
}
