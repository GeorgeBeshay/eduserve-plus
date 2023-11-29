package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Admin;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdminDAO {
    private final JdbcTemplate jdbcTemplate;
    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Admin readAdminById(int id) {
        try {
            String sql = """
                SELECT *
                FROM sys_admin
                WHERE admin_id = %d
                """.formatted(id);
            BeanPropertyRowMapper<Admin> rowMapper = new BeanPropertyRowMapper<>(Admin.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true); // to deal with null primitive data types.
            Admin admin = jdbcTemplate.queryForObject(sql, rowMapper);
//            System.out.println(admin);
            return admin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
