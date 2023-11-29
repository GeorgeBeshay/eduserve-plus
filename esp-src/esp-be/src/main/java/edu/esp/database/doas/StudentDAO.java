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
    RowMapper<Student> rowMapper = (rs, rowNum) -> {
        Student student = new Student();
        student.setStudentId(rs.getByte("student_id"));
        student.setStudentPwHash(rs.getInt("Student_pw_hash"));
        student.setDepartmentId(rs.getByte("dpt_id"));
        student.setStudentLevel(rs.getByte("student_level"));
        student.setGpa(rs.getFloat("gpa"));
        student.setStudentName(rs.getString("student_name"));
        student.setSsn(rs.getString("ssn"));
        student.setBirthDate(rs.getDate("bdate"));
        student.setStudentAddress(rs.getString("student_address"));
        student.setPhone(rs.getString("phone"));
        student.setLandline(rs.getString("landline"));
        student.setGender(rs.getBoolean("gender"));
        student.setEmail(rs.getString("email"));
        return student;
    };

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
        String sql = "SELECT * FROM student";
        BeanPropertyRowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true);
        return jdbcTemplate.query(sql, rowMapper);


    }
}
