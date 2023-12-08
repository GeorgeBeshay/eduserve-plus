package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class AdminDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdminDAO adminDAO;
    public InstructorDAO instructorDAO;
    private final Random random = new Random();
    private int insertedAdminId1;
    private final byte[] setupIds = {10,12,13,9,7,8};

    @BeforeAll
    public void setUp(){
        this.adminDAO = new AdminDAO(jdbcTemplate);

        jdbcTemplate.batchUpdate("""
            INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
            VALUES
                (%d, 65, 'Duplicate Test Admin', %d),
                (%d, 78, 'Read Test Admin', %d),
                (%d, 25, 'Delete Twice Test Admin', %d),
                (%d, 66, 'Delete Test Admin', %d),
                (%d, 456, 'Creator Test Admin', %d),
                (%d, 987, 'Created Test Admin', %d);
            """.formatted(
                setupIds[0], setupIds[0],
                setupIds[1], setupIds[0],
                setupIds[2], setupIds[2],
                setupIds[3], setupIds[0],
                setupIds[4], setupIds[0],
                setupIds[5], setupIds[4]
                )
        );
    }

    @AfterAll
    public void closingStatement(){
        // 2 Admins are deleted before admin with id = 10 in order to not violate FK constraints
        jdbcTemplate.batchUpdate("""
                DELETE FROM sys_admin WHERE admin_id = %d;
                DELETE FROM sys_admin WHERE admin_id = %d;
                DELETE FROM sys_admin WHERE admin_id = %d;
                """.formatted(insertedAdminId1, setupIds[1], setupIds[0]));
        System.out.println("\u001B[32m" + "All Admin DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Admin DAO - Valid Insertion Test Case")
    public void testCreateAdminSuccess(){
        insertedAdminId1 = random.nextInt(100) + 14;

        Admin newAdmin = new Admin();
        newAdmin.setAdminId((byte) insertedAdminId1);
        newAdmin.setAdminPwHash(9855);
        newAdmin.setCreatorAdminId(setupIds[0]);
        newAdmin.setAdminName("Test Admin One");

        assertTrue(this.adminDAO.createAdmin(newAdmin));
    }

    @Test
    @DisplayName("Admin DAO - Invalid Insertion Test Case (duplicate id)")
    public void testCreateAdminFailureDuplicateId(){

        Admin newAdmin = new Admin();
        newAdmin.setAdminId(setupIds[0]);     // setting duplicate id
        newAdmin.setCreatorAdminId(setupIds[0]);
        newAdmin.setAdminName("Invalid Insertion Admin");

        assertFalse(this.adminDAO.createAdmin(newAdmin));
    }

    @Test
    @DisplayName("Admin DAO - Invalid Insertion Test Case (invalid creator id)")
    public void testCreateAdminFailureInvalidCreator(){

        Admin newAdmin = new Admin();
        newAdmin.setAdminId((byte) 21);
        newAdmin.setCreatorAdminId((byte) 20); // setting invalid creator id
        newAdmin.setAdminName("Invalid Created Admin");

        assertFalse(this.adminDAO.createAdmin(newAdmin));
    }

    @Test
    @DisplayName("Admin DAO - read Admin with ID = 12")
    public void testReadAdminByValidId() {
        Admin admin = this.adminDAO.readAdminById(setupIds[1]);

        assertNotNull(admin);
        assertEquals(setupIds[1], admin.getAdminId());
        assertEquals("Read Test Admin", admin.getAdminName());
    }

    @Test
    @DisplayName("Admin DAO - read invalid Admin with ID = -1")
    public void testReadAdminByInvalidId() {
        Admin admin = this.adminDAO.readAdminById((byte) -1);

        assertNull(admin);
    }

    @Test
    @DisplayName("Admin DAO - select all admins")
    public void testSelectAll(){
        List<Admin> admins = this.adminDAO.SelectAll();

        assertNotEquals(null,admins);
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 9")
    public void testDeleteAdminByValidId() {
        assertTrue(this.adminDAO.deleteAdminById(setupIds[3]));
    }

    @Test
    @DisplayName("Admin DAO - delete invalid admin with ID = 5")
    public void testDeleteAdminByInvalidId() {
        assertFalse(this.adminDAO.deleteAdminById((byte)5));
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 13, then try to delete the same admin again")
    public void testDeleteAdminTwice() {
        assertTrue(this.adminDAO.deleteAdminById(setupIds[2]));
        assertFalse(this.adminDAO.deleteAdminById(setupIds[2]));
    }

    @Test
    @DisplayName("Admin DAO - try to delete admin before the admin he created")
    public void testDeleteCreatorBeforeCreatedAdmin() {
        assertFalse(this.adminDAO.deleteAdminById(setupIds[4]));
        assertTrue(this.adminDAO.deleteAdminById(setupIds[5]));
        assertTrue(this.adminDAO.deleteAdminById(setupIds[4]));
    }

//    Error: instructorDAO is null :(
//    @Test
//    @DisplayName("Admin DAO - testing database trigger to delete an unregistered instructor when it gets registered (signs up)")
//    public void TestUnregisteredInstructorTriggerAfterSigningUp(){
//        this.adminDAO.AddUnregisteredInstructors(2,456);
//        assertNotNull(this.adminDAO.ReadUnregisteredInstructor(2,456));
//        Instructor NewInstructor = new Instructor(2,456);
//        this.instructorDAO.signUpInstructor(2,456,NewInstructor);
//        //signUpInstructor will eventually insert a new instructor in the instructor table and the DB trigger will delete it from the unregistered_instructor table
//        //so we check that when searching for that instructor in the table of the unregistered_instructor we will get a null value.
//        assertNull(this.adminDAO.ReadUnregisteredInstructor(2,456));
//
//    }
}

