package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class AdminDAO {
    private final JdbcTemplate jdbcTemplate;
    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createAdmin(Admin newAdmin) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("sys_admin");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newAdmin);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false; // Return a meaningful response indicating failure
        }
    }

}
