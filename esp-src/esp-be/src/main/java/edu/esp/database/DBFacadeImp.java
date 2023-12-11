package edu.esp.database;

import edu.esp.database.daos.AdminDAO;
import edu.esp.database.daos.InstructorDAO;
import edu.esp.database.daos.StudentDAO;
import edu.esp.database.daos.CourseDAO;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.Student;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBFacadeImp {

    private final JdbcTemplate jdbcTemplate;
    private final StudentDAO studentDAO;
    private final InstructorDAO instructorDAO;
    private final AdminDAO adminDAO;
    private final CourseDAO CourseDAO;

    @Autowired
    public DBFacadeImp(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.studentDAO = new StudentDAO(this.jdbcTemplate);
        this.instructorDAO = new InstructorDAO(this.jdbcTemplate);
        this.adminDAO = new AdminDAO(this.jdbcTemplate);
        this.CourseDAO = new CourseDAO(this.jdbcTemplate);
    }        
   
    public boolean createStudent(Student st){
        return this.studentDAO.createStudent(st);
    }
  
    public boolean createAdmin(Admin admin){
        return this.adminDAO.createAdmin(admin);
    }
  
    public boolean createInstructor(Instructor instructor){
        return this.instructorDAO.createInstructor(instructor);
    }
    
    public Student loadStudentData(int studentId){
        return this.studentDAO.readStudentById(studentId);
    }

    public Admin loadAdmin(byte adminId) {
        return this.adminDAO.readAdminById(adminId);
    }

    public Instructor loadInstructorData(int instructorId) {
        return this.instructorDAO.readInstructorById(instructorId);
    }

    public boolean deleteStudent(int studentId){
        return this.studentDAO.deleteStudentById(studentId);
    }

    public boolean deleteInstructor(int instructorId){
        return this.instructorDAO.deleteInstructorById(instructorId);
    }

    public boolean deleteAdmin(byte adminId){
        return this.adminDAO.deleteAdminById(adminId);
    }

    public boolean signUpInstructor(int id, int hash, Instructor instructor) {
        return this.instructorDAO.signUpInstructor(id, hash, instructor);
    }

    public boolean signUpStudent(int id, int hash, Student student) {
        return this.studentDAO.signUpStudent(id, hash, student);
    }

    public boolean AddNewUnregisteredInstructor(UnregisteredInstructor unregisteredInstructor) {
        return this.instructorDAO.AddUnregisteredInstructors(unregisteredInstructor);

    }
    public boolean addNewCourse( Course newCourse ){
        return this.CourseDAO.addNewCourse( newCourse );

    }
}
