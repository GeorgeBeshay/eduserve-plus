package edu.esp.database.doas;


import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class InstructorDAO {
    
    private final JdbcTemplate jdbcTemplate;

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
