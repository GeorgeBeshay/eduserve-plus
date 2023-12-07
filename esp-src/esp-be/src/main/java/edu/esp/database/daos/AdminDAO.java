package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Admin;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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

    public List<Admin> SelectAll() {
        try {
            String sql = "SELECT * FROM sys_admin";
            BeanPropertyRowMapper<Admin> rowMapper = new BeanPropertyRowMapper<>(Admin.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.query(sql, rowMapper);
        }
        catch (Exception e) {
            System.out.println("Error in selectAllAdmins: " + e.getMessage());
            return null;
        }

    }

    public boolean deleteAdminById(int id) {
        try{
            String sql = """
                    DELETE FROM sys_admin
                    WHERE admin_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


}
