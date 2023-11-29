package edu.esp.database;

import edu.esp.database.doas.StudentDAO;
import edu.esp.system_entities.system_users.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBFacade_Impl {

    private final JdbcTemplate jdbcTemplate;
    private final StudentDAO studentDAO;

    @Autowired
    public DBFacade_Impl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.studentDAO = new StudentDAO(this.jdbcTemplate);
    }

    public void getStudentById(){
        Student st = this.studentDAO.readStudentById(1);
    }

}
