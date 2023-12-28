package edu.esp.database.daos;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

public abstract class DAO <T> {
    protected final JdbcTemplate jdbcTemplate;
    private final Class<T> clazz;
    protected final BeanPropertyRowMapper<T> rowMapper;

    public DAO(JdbcTemplate jdbcTemplate, Class<T> clazz) {
        this.jdbcTemplate = jdbcTemplate;
        this.clazz = clazz;
        this.rowMapper = new BeanPropertyRowMapper<>(clazz);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public BeanPropertyRowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public int getCurrentSeason() {
        Integer ret = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        return Objects.requireNonNullElse(ret, -1);
    }

    public String getCurrentYear() {
        String ret = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);
        return Objects.requireNonNullElse(ret, "");
    }
}
