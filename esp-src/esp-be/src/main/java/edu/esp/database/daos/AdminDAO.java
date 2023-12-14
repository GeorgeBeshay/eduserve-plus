package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;

public class AdminDAO extends DAO <Admin> {

    public AdminDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Admin.class);
    }

    public Admin readAdminById(byte id) {
        try {
            String sql = """
                SELECT *
                FROM sys_admin
                WHERE admin_id = %d
                """.formatted(id);

            return jdbcTemplate.queryForObject(sql, rowMapper);

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in readAdminByID: " + e.getMessage(), 1);
            return null;

        }
    }

    public boolean createAdmin(Admin newAdmin) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("sys_admin");
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newAdmin);

            return jdbcInsert.execute(parameterSource) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error had occurred in admin record insertion: " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure

        }
    }

    public List<Admin> SelectAll() {
        try {
            String sql = "SELECT * FROM sys_admin";

            return jdbcTemplate.query(sql, rowMapper);
        }
        catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in selectAllAdmins: " + e.getMessage(), 1);
            return null;
        }

    }

    public boolean deleteAdminById(byte id) {
        try {
            String sql = """
                    DELETE FROM sys_admin
                    WHERE admin_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        } catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(), e.getMessage(), 1);
            return false;
        }
    }

}
