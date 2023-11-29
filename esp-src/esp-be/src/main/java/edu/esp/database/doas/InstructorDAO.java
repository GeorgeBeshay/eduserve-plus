package edu.esp.database.doas;

import org.springframework.jdbc.core.JdbcTemplate;

public class InstructorDAO {
    private final JdbcTemplate jdbcTemplate;

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
