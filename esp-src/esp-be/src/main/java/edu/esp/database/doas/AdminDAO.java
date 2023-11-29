package edu.esp.database.doas;


import edu.esp.system_entities.system_users.Admin;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class AdminDAO {
    private final JdbcTemplate jdbcTemplate;

    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

        RowMapper<Admin> rowMapper = (rs, rowNum) -> {
        Admin admin = new Admin();
        admin.setAdminId(rs.getByte("admin_id"));
        admin.setAdminPwHash(rs.getInt("admin_pw_hash"));
        admin.setCreatorAdminId(rs.getByte("creator_admin_id"));
        admin.setAdminName(rs.getString("admin_name"));
        return admin;
    };
    public List<Admin> SelectAll() {
        String sql = "SELECT * FROM sys_admin";
        BeanPropertyRowMapper<Admin> rowMapper = new BeanPropertyRowMapper<>(Admin.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true);
        return jdbcTemplate.query(sql, rowMapper);


    }
}