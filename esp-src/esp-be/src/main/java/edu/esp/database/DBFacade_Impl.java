package edu.esp.database;

import edu.esp.database.doas.AdminDAO;
import edu.esp.database.doas.InstructorDAO;
import edu.esp.database.doas.StudentDAO;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBFacade_Impl {

    private final JdbcTemplate jdbcTemplate;
    private final StudentDA
    private final InstructorDAO instructorDAO;
    private final AdminDAO adminDAO;

    @Autowired
    public DBFacade_Impl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.studentDAO = new StudentDAO(this.jdbcTemplate);
        this.instructorDAO = new InstructorDAO(this.jdbcTemplate);
        this.adminDAO = new AdminDAO(this.jdbcTemplate);
    }        
   
    public boolean createStudent(Student st){     // to be modified to take the correct student object.
        boolean operationsResult = this.studentDAO.createStudent(st);
        System.out.println(operationsResult);
        return operationsResult;
    }
  
    public boolean createAdmin(Admin admin){
        boolean operationResult = this.adminDAO.createAdmin(admin);
        System.out.println(operationResult);
        return operationResult;
    }
  
    public boolean createInstructor(Instructor instructor){
        boolean operationResult = this.instructorDAO.createInstructor(instructor);
        System.out.println(operationResult);
        return operationResult;
    }
    
    public Student loadStudentData(int studentId){
        return this.studentDAO.readStudentById(studentId);
    }

    public Admin loadAdmin(int adminId) {
        return this.adminDAO.readAdminById(adminId);
    }

    public Instructor loadInstructorData(int instructorId) {
        return this.instructorDAO.readInstructorById(instructorId);
    }
}
