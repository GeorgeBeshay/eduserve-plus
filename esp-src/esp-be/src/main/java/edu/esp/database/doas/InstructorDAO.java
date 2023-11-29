package edu.esp.database.doas;

import edu.esp.system_entities.system_users.Instructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class InstructorDAO {
    private final JdbcTemplate jdbcTemplate;

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Instructor readInstructorById(int id) {
        try {
            String sql = """
                SELECT *
                FROM instructor
                WHERE instructor_id = %d
                """.formatted(id);
            BeanPropertyRowMapper<Instructor> rowMapper = new BeanPropertyRowMapper<>(Instructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true); // to deal with null primitive data types.
            Instructor instructor = jdbcTemplate.queryForObject(sql, rowMapper);
//            System.out.println(instructor);
            return instructor;
        } catch (Exception e) {
            System.out.println("Error in readInstructorByID: " + e.getMessage());
            return null;
        }
    }
  
    public boolean createInstructor(Instructor newInstructor) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("instructor");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newInstructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch (Exception ex) {
            System.out.println("\u001B[35m" + "Error had occurred in admin record insertion: " + ex.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
    }

    public boolean deleteInstructorById(int id) {
        try{
            String sql = """
                    DELETE FROM instructor
                    WHERE instructor_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
