package edu.esp.database.daos;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
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

    /**
     *
     * @param InstructorId instructor's id as listed in university's real system
     * @param OTP the one time password given to the instructor so he can sign up to the system and choose a password later
     * @return true if the instructor registrations was successful, false otherwise
     */
    public boolean AddUnregisteredInstructors(int InstructorId, int OTP ){
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("unregistered_instructor");
            Instructor instructor = new Instructor(InstructorId,OTP);


            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(instructor);
            int rowsAffected = jdbcInsert.execute(parameterSource);

            return rowsAffected > 0;
        } catch(Exception exception){
            System.out.println("\u001B[35m" + "Error had occurred in registering new instructor to database record " + exception.getMessage() + "\u001B[0m");
            return false ; // Return a meaningful response indicating failure

    }
}


    public Admin readAdminById(byte id) {
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

    public boolean deleteAdminById(byte id) {
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
    public Instructor ReadUnregisteredInstructor(int InstructorId, int InstructorOTP){
        try{
            String sql = """
                    SELECT *
                    FROM unregistered_instructor
                    WHERE instructor_id = %d""".formatted(InstructorId);
            BeanPropertyRowMapper<Instructor> rowMapper = new BeanPropertyRowMapper<>(Instructor.class);
            rowMapper.setPrimitivesDefaultedForNullValue(true);
            Instructor instructor = jdbcTemplate.queryForObject(sql, rowMapper);
            return instructor;
        }catch (Exception e) {
            System.out.println("Error in ReadUnregisteredInstructorByID: " + e.getMessage());
            return null;
        }
    }


}
