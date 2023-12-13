package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Student;
import edu.esp.system_entities.system_users.UnregisteredStudent;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
            return jdbcTemplate.queryForObject(sql, rowMapper);
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

    /**
     * @param id The ID which the student has entered while signing up
     * @param tempHash The hash of the OTP which the student has entered while signing up
     * @param registeredStudent The object which encapsulates the data that the student has entered while signing up not including the department ID
     * @return Returns a boolean indicating the success of the signup operation
     */
    public boolean signUpStudent(int id, int tempHash, Student registeredStudent){
        try {
            UnregisteredStudent unregisteredStudent = readUnregisteredStudentById(id);
            // If the input temporary hash is not the same as the unregistered password hash, reject operation
            if (unregisteredStudent == null || unregisteredStudent.getStudentTempPwHash() != tempHash) return false;
            // Add record to students table and rely on the DB trigger to delete that record from the unregistered_students
            registeredStudent.setDptId(unregisteredStudent.getDptId());
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
