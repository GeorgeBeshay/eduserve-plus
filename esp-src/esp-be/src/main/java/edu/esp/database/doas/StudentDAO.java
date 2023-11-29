package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
public class StudentDAO {

    private final JdbcTemplate jdbcTemplate;

    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createStudent() {

    }

    public Student readStudentById(int id) {
        String sql = """
                SELECT *
                FROM student
                WHERE student_id = %d
                """.formatted(id);
        BeanPropertyRowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true);     // to deal with null primitive data types.
        Student st = jdbcTemplate.queryForObject(sql, rowMapper);
        System.out.println(st);
        return st;
    }
    public void updateStudent(){

    }

    public void deleteStudentById(int id){

    }
}
