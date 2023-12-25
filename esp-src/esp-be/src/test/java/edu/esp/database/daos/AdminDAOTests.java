package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Admin;
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
    private final Random random = new Random();

    @BeforeAll
    public void setUp() {
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    @AfterAll
    public void closingStatement() {
        System.out.println("\u001B[32m" + "All Admin DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Admin DAO - Valid Insertion Test Case")
    public void testCreateAdminSuccess() {
        // Create an admin object with test data
        byte insertedAdminId = (byte) random.nextInt(1, 10);
        Admin newAdmin = new Admin(insertedAdminId, 9855, "Test Admin", (byte)0);

        assertTrue(this.adminDAO.createAdmin(newAdmin));

        // Delete the used admin object
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id = %d;".formatted(insertedAdminId));
    }

    @Test
    @DisplayName("Admin DAO - Invalid Insertion Test Case with duplicate id")
    public void testCreateAdminFailureDuplicateId() {
        // Create two admin objects with same ID and different test data
        byte dupId = (byte) random.nextInt(1, 10);
        Admin newAdmin = new Admin(dupId, 68432, "Test Admin", (byte) 0);
        Admin dupAdmin = new Admin(dupId, -651, "Duplicate ID Admin", (byte) 0);

        assertTrue(this.adminDAO.createAdmin(newAdmin));
        assertFalse(this.adminDAO.createAdmin(dupAdmin));

        // Delete the used admin object
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id = %d;".formatted(dupId));
    }

    @Test
    @DisplayName("Admin DAO - Invalid Insertion Test Case with invalid creator id")
    public void testCreateAdminFailureInvalidCreator() {
        // Create an admin object with an invalid creator admin ID
        byte randomId = (byte) random.nextInt(1, 8);
        Admin newAdmin = new Admin(randomId, 6500, "Test Admin", (byte) 9);

        assertFalse(this.adminDAO.createAdmin(newAdmin));
    }

    @Test
    @DisplayName("Admin DAO - read Admin with ID = 8")
    public void testReadAdminByValidId() {
        // Insert two admin object with ID = 7,8 where one has created the other
        jdbcTemplate.update("""
                INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
                VALUES
                    (7, 78, 'Creator Admin', 0),
                    (8, 78, 'Read Test Admin', 7);
                """);

        Admin admin = this.adminDAO.readAdminById((byte)8);

        assertEquals(8, admin.getAdminId());
        assertEquals(78, admin.getAdminPwHash());
        assertEquals("Read Test Admin", admin.getAdminName());
        assertEquals(7, admin.getCreatorAdminId());

        // Delete the used admin objects
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id IN (8,7);");
    }

    @Test
    @DisplayName("Admin DAO - read invalid Admin with ID = -1")
    public void testReadAdminByInvalidId() {
        assertNull(this.adminDAO.readAdminById((byte) -1));
    }

    @Test
    @DisplayName("Admin DAO - create an admin  object then read it with DAO methods")
    public void testCreateThenReadAdmin() {
        Admin admin1 = new Admin((byte)2, 101, "Test Admin", (byte)0);
        Admin admin2 = new Admin((byte)3, -6504, "Read Test Admin", (byte)2);

        assertTrue(this.adminDAO.createAdmin(admin1));
        assertTrue(this.adminDAO.createAdmin(admin2));

        Admin admin = this.adminDAO.readAdminById((byte)3);

        assertEquals(3, admin.getAdminId());
        assertEquals(-6504, admin.getAdminPwHash());
        assertEquals("Read Test Admin", admin.getAdminName());
        assertEquals(2, admin.getCreatorAdminId());

        // Delete the used admin objects
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id IN (2,3);");
    }

    @Test
    @DisplayName("Admin DAO - select all admins")
    public void testSelectAll() {
        // Insert a list of admins
        jdbcTemplate.update("""
                INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
                VALUES
                    (1, 1003, 'Admin One', 0),
                    (2, 78, 'Admin Two', 1),
                    (3, 28, 'Admin Three', 2);
                """);

        List<Admin> admins = this.adminDAO.SelectAll();

        assertNotNull(admins);
        assertTrue(admins.size() >= 4); // Size is at least 4 (with the root admin)

        // Check that the list contains the inserted IDs
        boolean[] containsId = {false,false,false};
        int id;
        for (Admin admin : admins) {
            id = admin.getAdminId();
            if (id == 1 || id == 2 || id == 3)
                containsId[id-1] = true;
        }
        assertArrayEquals(new boolean[]{true,true,true}, containsId);

        // Delete the admin list
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id IN (1,2,3);");
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 9")
    public void testDeleteAdminByValidId() {
        // Insert two admin object with ID = 9
        jdbcTemplate.update("""
                INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
                VALUES (9, -67801, 'Test Admin', 0);
                """);

        assertTrue(this.adminDAO.deleteAdminById((byte)9));
    }

    @Test
    @DisplayName("Admin DAO - delete invalid admin with ID = 5")
    public void testDeleteAdminByInvalidId() {
        assertFalse(this.adminDAO.deleteAdminById((byte)5));
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 5, then try to delete the same admin again")
    public void testDeleteAdminTwice() {
        // Insert two admin object with ID = 5
        jdbcTemplate.update("""
                INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
                VALUES (5, 99066, 'Test Admin', 0);
                """);

        assertTrue(this.adminDAO.deleteAdminById((byte)5));
        assertFalse(this.adminDAO.deleteAdminById((byte)5));
    }

    @Test
    @DisplayName("Admin DAO - delete admin before the admin they created")
    public void testDeleteCreatorBeforeCreatedAdmin() {
        // Insert two admin object with ID = 7,8 where one has created the other
        jdbcTemplate.update("""
                INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
                VALUES
                    (7, 78, 'Creator Admin', 0),
                    (8, 78, 'Created Admin', 7);
                """);

        // Created admins' creator admins are set to 0 by a trigger on parent record deletion
        assertTrue(this.adminDAO.deleteAdminById((byte)7));
        assertEquals(0, this.adminDAO.readAdminById((byte)8).getCreatorAdminId());
        assertTrue(this.adminDAO.deleteAdminById((byte)8));
    }
}

