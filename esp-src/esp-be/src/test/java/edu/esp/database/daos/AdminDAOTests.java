package edu.esp.database.daos;

import edu.esp.be.EspBeApplication;
import edu.esp.database.doas.AdminDAO;
import edu.esp.system_entities.system_users.Admin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EspBeApplication.class)
public class AdminDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AdminDAO adminDAO;
    private final Random random = new Random();

    @BeforeEach
    public void setUp(){
        this.adminDAO = new AdminDAO(jdbcTemplate);
    }

    @AfterAll
    public static void closingStatement(){
        System.out.println("\u001B[32m" + "All Admin DAO test cases have been executed." + "\u001B[0m");
    }

    @Test
    @DisplayName("Admin DAO - Valid Insertion Test Case")
    public void testCreateAdmin_Success(){
        int randomNumber = random.nextInt(100) + 2;

        Admin newAdmin = new Admin();
        newAdmin.setAdminId((byte) randomNumber);
        newAdmin.setCreatorAdminId((byte) 1);

        assertTrue(this.adminDAO.createAdmin(newAdmin));

        // delete the inserted record.
    }

    @Test
    @DisplayName("Admin DAO - Invalid Insertion Test Case (duplicate id)")
    public void testCreateAdmin_Failure_duplicate_id(){

        Admin newAdmin = new Admin();
        newAdmin.setAdminId((byte) 1);     // setting duplicate id
        newAdmin.setCreatorAdminId((byte) 1);

        assertFalse(this.adminDAO.createAdmin(newAdmin));
    }

    @Test
    @DisplayName("Admin DAO - read Admin with ID = 1")
    public void testReadAdminById1() {
        Admin admin = this.adminDAO.readAdminById(1);

        assertNotNull(admin);
        assertEquals(1, admin.getAdminId());
    }

    @Test
    @DisplayName("Admin DAO - read invalid Admin with ID = -1")
    public void testReadAdminById2() {
        Admin admin = this.adminDAO.readAdminById(-1);

        assertNull(admin);
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 2")
    public void deleteAdminById1() {
        assertTrue(this.adminDAO.deleteAdminById(2));
    }

    @Test
    @DisplayName("Admin DAO - delete invalid admin with ID = 5")
    public void deleteAdminById2() {
        assertFalse(this.adminDAO.deleteAdminById(2));
    }

    @Test
    @DisplayName("Admin DAO - delete valid admin with ID = 3, then try to delete the same admin again")
    public void deleteStudentById3() {
        assertTrue(this.adminDAO.deleteAdminById(3));
        assertFalse(this.adminDAO.deleteAdminById(3));
    }
}
