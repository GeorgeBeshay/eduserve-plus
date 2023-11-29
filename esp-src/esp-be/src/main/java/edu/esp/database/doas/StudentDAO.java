package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
            Student st = jdbcTemplate.queryForObject(sql, rowMapper);
//            System.out.println(st);
            return st;
        }
        catch (Exception e) {
            System.out.println("Error in readStudentByID: " + e.getMessage());
            return null;
        }
    }
    public void updateStudent(){

    }

    public void deleteStudentById(int id){

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
}
