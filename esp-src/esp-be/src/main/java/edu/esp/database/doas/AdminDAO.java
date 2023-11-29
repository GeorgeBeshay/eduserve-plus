package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Admin;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import edu.esp.system_entities.system_users.Admin;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

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
            System.out.println("Error in readAdminByID: " + e.getMessage());
            return null;
        }
    }

    public boolean createAdmin(Admin newAdmin) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("sys_admin");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newAdmin);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println("\u001B[35m" + "Error had occurred in admin record insertion: " + ex.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
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
