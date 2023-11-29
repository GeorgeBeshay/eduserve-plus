package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Instructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class InstructorDAO {

    private final JdbcTemplate jdbcTemplate;

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Instructor readInstructorById(int id) {
        try {
            String sql = """
                SELECT *
                FROM instructor
                WHERE instructor_id = %d
                """.formatted(id);
            BeanPropertyRowMapper<Instructor> rowMapper = new BeanPropertyRowMapper<>(Instructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true); // to deal with null primitive data types.
            Instructor instructor = jdbcTemplate.queryForObject(sql, rowMapper);
//            System.out.println(instructor);
            return instructor;
        } catch (Exception e) {
            System.out.println("Error in readInstructorByID: " + e.getMessage());
            return null;
        }
    }

    public boolean createInstructor(Instructor newInstructor) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("instructor");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newInstructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println("\u001B[35m" + "Error had occurred in admin record insertion: " + ex.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
    }

    RowMapper<Instructor> rowMapper = (rs, rowNum) -> {
        Instructor instructor = new Instructor();
        instructor.setInstructorId(rs.getByte("instructor_id"));
        instructor.setInstructorPwHash(rs.getInt("instructor_pw_hash"));
        instructor.setDepartmentId(rs.getByte("dpt_id"));
        instructor.setPhone(rs.getString("phone"));
        instructor.setEmail(rs.getString("email"));
        instructor.setOfficeHours(rs.getString("office_hrs"));
        return instructor;
    };
    public List<Instructor> SelectAll() {
        String sql = "SELECT * FROM instructor";
        BeanPropertyRowMapper<Instructor> rowMapper = new BeanPropertyRowMapper<>(Instructor.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true);
        return jdbcTemplate.query(sql, rowMapper);

    }
}
