package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.List;

public class InstructorDAO extends DAO<Instructor> {

    public InstructorDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Instructor.class);
    }

    public Instructor readInstructorById(int id) {
        try {
            String sql = """
                SELECT *
                FROM instructor
                WHERE instructor_id = %d
                """.formatted(id);

            return jdbcTemplate.queryForObject(sql, rowMapper);
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in readInstructorByID: " + e.getMessage(), 1);
            return null;
        }
    }

    public boolean createInstructor(Instructor newInstructor) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("instructor");
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(newInstructor);

            return jdbcInsert.execute(parameterSource) > 0;

        } catch (Exception ex) {
            Logger.logMsgFrom(this.getClass().getName(), "Error had occurred in instructor record insertion: " + ex.getMessage(), 1);
            return false; // Return a meaningful response indicating failure
        }
    }

    public List<Instructor> SelectAll() {
        try {
            String sql = "SELECT * FROM instructor";

            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error in selectAllInstructors: " + e.getMessage(), 1);
            return null;
        }
    }

    public boolean deleteInstructorById(int id) {
        try {
            String sql = """
                    DELETE FROM instructor
                    WHERE instructor_id = %d
                    """.formatted(id);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), e.getMessage(), 1);
            return false;
        }
    }

    /**
     * @param id The ID which the instructor has entered while signing up
     * @param tempHash The hash of the OTP which the instructor has entered while signing up
     * @param registeredInstructor The object which encapsulates the data that the instructor has entered while signing up not including the department ID
     * @return Returns a boolean indicating the success of the signup operation
     */
    public boolean signUpInstructor(int id, int tempHash, Instructor registeredInstructor) {
        try {
            UnregisteredInstructor unregisteredInstructor = readUnregisteredInstructorById(id);
            // If the input temporary hash is not the same as the unregistered password hash, reject operation
            if (unregisteredInstructor == null || unregisteredInstructor.getInstructorTempPwHash() != tempHash) return false;
            // Add record to instructors table and rely on the DB trigger to delete that record from the unregistered_instructors
            registeredInstructor.setDptId(unregisteredInstructor.getDptId());
            return createInstructor(registeredInstructor);
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error had occurred in instructor sign up: " + e.getMessage(), 1);
            return false; // Return a meaningful response indicating failure
        }
    }

    /**
     *
     * @param unregisteredInstructor Object is passed to the function to add it to the unregistered_instructor table in the database
     * @return true if the insertion of the unregistered instructor into the unregistered_instructor table succeeded,
     * false otherwise.
     */
    public boolean createUnregisteredInstructor(UnregisteredInstructor unregisteredInstructor) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("unregistered_instructor");

            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(unregisteredInstructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch(Exception exception) {
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in adding a new unregistered instructor to the system.",1);
            return false ; // Return a meaningful response indicating failure
        }
    }

    public UnregisteredInstructor readUnregisteredInstructorById(int InstructorId) {
        try {
            String sql = """
                    SELECT *
                    FROM unregistered_instructor
                    WHERE instructor_id = %d""".formatted(InstructorId);
            BeanPropertyRowMapper<UnregisteredInstructor> rowMapper = new BeanPropertyRowMapper<>(UnregisteredInstructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.queryForObject(sql, rowMapper);
        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in reading an unregistered instructor from the system.",1);
            return null;
        }
    }

    public boolean deleteUnregisteredInstructorById(int unregisteredInstructorId) {
        try{
            String sql = """
                    DELETE FROM unregistered_instructor
                    WHERE instructor_id = %d
                    """.formatted(unregisteredInstructorId);
            int rowsAffected = jdbcTemplate.update(sql);
            return rowsAffected > 0;
        }catch (Exception e){
            Logger.logMsgFrom(this.getClass().getName(),"Error deleting an unregistered instructor by his id.",1);
            return false;
        }
    }

    public List<UnregisteredInstructor> getAllUnregisteredInstructors() {
        try {
            BeanPropertyRowMapper<UnregisteredInstructor> rm = new BeanPropertyRowMapper<>(UnregisteredInstructor.class);
            this.rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.query("SELECT * FROM unregistered_instructor;",rm);
        }
        catch (Exception error) {
            Logger.logMsgFrom(this.getClass().getName(), error.getMessage(), 1);
            return null;
        }
    }
}
