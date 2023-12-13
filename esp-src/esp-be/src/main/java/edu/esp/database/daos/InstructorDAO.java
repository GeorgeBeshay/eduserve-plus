package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.util.List;

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
            System.out.println("\u001B[35m" + "Error had occurred in instructor record insertion: " + ex.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
    }

    public List<Instructor> SelectAll() {
        try {
            String sql = "SELECT * FROM instructor";
            BeanPropertyRowMapper<Instructor> rowMapper = new BeanPropertyRowMapper<>(Instructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            return jdbcTemplate.query(sql, rowMapper);
        }
        catch (Exception e) {
            System.out.println("Error in selectAllInstructors: " + e.getMessage());
            return null;
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

    public boolean signUpInstructor(int id, int tempHash, Instructor registeredInstructor){
        try{
            int passwordHash;
            // Retrieve ID and temporary password hash from unregistered instructors table
            SqlRowSet unregisteredInstructor = jdbcTemplate.queryForRowSet("""
                    SELECT *
                    FROM unregistered_instructor
                    WHERE instructor_id = %d
                    """.formatted(id));
            // If no such record exists, reject operation
            if (unregisteredInstructor.next())
                passwordHash = unregisteredInstructor.getInt("Instructor_temp_pw_hash");
            else
                return false;
            // If the input temporary hash is not the same as the unregistered password hash, reject operation
            if (tempHash != passwordHash) return false;
            // Add record to instructors table and rely on the DB trigger to delete that record from the unregistered_instructors
            createInstructor(registeredInstructor);
            return true;
        }catch (Exception e){
            System.out.println("\u001B[35m" + "Error had occurred in instructor sign up: " + e.getMessage() + "\u001B[0m");
            return false; // Return a meaningful response indicating failure
        }
    }

    /**
     *
     * @param unregisteredInstructor Object is passed to the function to add it to the unregistered_instructor table in the database
     * @return true if the insertion of the unregistered instructor into the unregistered_instructor table succeeded,
     * false otherwise.
     */
    public boolean addUnregisteredInstructors(UnregisteredInstructor unregisteredInstructor ){
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("unregistered_instructor");
//            UnregisteredInstructor instructor = new UnregisteredInstructor(instructor_id,instructor_temp_pw_hash);


            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(unregisteredInstructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch(Exception exception){
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in adding a new unregistered instructor to the system.",1);
            return false ; // Return a meaningful response indicating failure

        }
    }
    public UnregisteredInstructor readUnregisteredInstructor(int InstructorId){
        try{
            String sql = """
                    SELECT *
                    FROM unregistered_instructor
                    WHERE instructor_id = %d""".formatted(InstructorId);
            BeanPropertyRowMapper<UnregisteredInstructor> rowMapper = new BeanPropertyRowMapper<>(UnregisteredInstructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            UnregisteredInstructor unregisteredInstructor = jdbcTemplate.queryForObject(sql, rowMapper);
            return unregisteredInstructor;
        }catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(),"Error occurred in reading an unregistered instructor from the system.",1);
            return null;
        }
    }
    public Boolean deleteUnregisteredInstructorById(int unregisteredInstructorId){
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
}
