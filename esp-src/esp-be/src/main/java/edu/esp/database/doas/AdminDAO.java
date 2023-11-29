package edu.esp.database.doas;

import org.springframework.jdbc.core.JdbcTemplate;

public class AdminDAO {
    private final JdbcTemplate jdbcTemplate;
    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
