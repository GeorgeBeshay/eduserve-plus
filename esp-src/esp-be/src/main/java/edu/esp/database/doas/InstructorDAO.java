package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class InstructorDAO {
    private final JdbcTemplate jdbcTemplate;

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createInstructor(Instructor newInstructor) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("instructor");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newInstructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false; // Return a meaningful response indicating failure
        }
    }
}
