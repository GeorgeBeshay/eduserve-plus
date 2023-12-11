package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Student;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.system_entities.system_users.UnregisteredStudent;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class StudentDAO {

    private final JdbcTemplate jdbcTemplate;

    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createStudent(Student newStudent) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("student");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newStudent);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println("\u001B[35m" + "Error had occurred in student record insertion: " + ex.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
    }

    public Student readStudentById(int id) {
        try {
            String sql = """
                SELECT *
                FROM student
                WHERE student_id = %d
                """.formatted(id);
            BeanPropertyRowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);     // to deal with null primitive data types.
            Student st = jdbcTemplate.queryForObject(sql, rowMapper);
//            System.out.println(st);
            return st;
        }
        catch (Exception e) {
            System.out.println("Error in readStudentByID: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteStudentById(int id){
        try{
            String sql = """
                    DELETE FROM student
                    WHERE student_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Student> SelectAll() {
        try {
            String sql = "SELECT * FROM student";
            BeanPropertyRowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.query(sql, rowMapper);
        }
        catch (Exception e) {
            System.out.println("Error in selectAllStudents: " + e.getMessage());
            return null;
        }
    }

    public boolean signUpStudent(int id, int tempHash, Student registeredStudent){
        try {
            int passwordHash;
            // Retrieve ID and temporary password hash from unregistered students table
            SqlRowSet unregisteredStudent = jdbcTemplate.queryForRowSet("""
                    SELECT *
                    FROM unregistered_student
                    WHERE student_id = %d
                    """.formatted(id));
            // If no such record exists, reject operation
            if (unregisteredStudent.next())
                passwordHash = unregisteredStudent.getInt("student_temp_pw_hash");
            else
                return false;
            // If the input temporary hash is not the same as the unregistered password hash, reject operation
            if (tempHash != passwordHash) return false;
            // Add record to students table and rely on the DB trigger to delete that record from the unregistered_students
            return createStudent(registeredStudent);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error had occurred in student sign up: " + e.getMessage(), 1);
            return false; // Return a meaningful response indicating failure
        }
    }

    public boolean createUnregisteredStudent(UnregisteredStudent unregisteredStudent) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("unregistered_student");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(unregisteredStudent);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in adding a new unregistered student to the system.",1);
            return false ; // Return a meaningful response indicating failure
        }
    }

    public UnregisteredStudent readUnregisteredStudentById(int id) {
        try {
            String sql = """
                    SELECT *
                    FROM unregistered_student
                    WHERE student_id = %d""".formatted(id);
            BeanPropertyRowMapper<UnregisteredStudent> rowMapper = new BeanPropertyRowMapper<>(UnregisteredStudent.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.queryForObject(sql, rowMapper);
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in reading an unregistered student from the system.",1);
            return null;
        }
    }

    public boolean deleteUnregisteredStudentById(int id) {
        try{
            String sql = """
                    DELETE FROM unregistered_student
                    WHERE student_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        }catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(),"Error deleting an unregistered student by id.",1);
            return false;
        }
    }
}
