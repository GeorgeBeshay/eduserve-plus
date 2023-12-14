package edu.esp.database.daos;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
