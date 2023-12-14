package edu.esp.services;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Admin;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class AdminServicesTests {

    private final JdbcTemplate jdbcTemplate;
    private AdminServices adminServices;

    @Autowired
    public AdminServicesTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setUp() {
        this.adminServices = new AdminServices(jdbcTemplate);
        assert (
                jdbcTemplate.queryForList(
                        "SELECT * FROM sys_admin WHERE admin_id BETWEEN ? AND ?",
                        (byte) 1,
                        (byte) 10)
        ).isEmpty() : "Check your DB state, records with testing id were found!";

    }


    @Test
    @DisplayName("Method addNewAdmin() - Passing a null object.")
    public void addNewAdminNullMap() {
        // Arrange
        Map<String, Object> requestMap = null;

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing an empty map.")
    public void addNewAdminEmptyMap() {
        // Arrange
        Map<String, Object> requestMap = new HashMap<>();

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing a map with a missing 'admin' field.")
    public void addNewAdminMapMissingAdminField() {
        // Arrange
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("adminPw", "awesomePassword");

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing a map with a missing 'adminPw' field.")
    public void addNewAdminMapMissingAdminPwField() {
        // Arrange
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", new Admin());

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing a map with an admin initialized using an empty constructor.")
    public void addNewAdminMapAdminAttributesNonTouched() {
        // Arrange
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", new Admin());
        requestMap.put("adminPw", "awesomePassword");

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing a map containing valid fields.")
    public void addNewAdminValidMap() {
        // Arrange
        byte adminId = 2;
        byte creatorAdminId = 0;

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", new Admin(adminId, 123, "TEST ADMIN", creatorAdminId));
        requestMap.put("adminPw", "awesomePassword");

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertTrue(result);

        // Clean
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id = ?", adminId);

    }

    @Test
    @DisplayName("Method addNewAdmin() - Passing a map containing an admin that already exists.")
    public void addNewAdminDuplicateAdmin() {
        // Arrange
        byte adminId = 2;
        byte creatorAdminId = 0;

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", new Admin(adminId, 123, "TEST ADMIN", creatorAdminId));
        requestMap.put("adminPw", "awesomePassword");

        // Act
        boolean result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertTrue(result);

        // Act - Again, to check for behavior against duplicate insertions.
        result = adminServices.addNewAdmin(requestMap);

        // Assert
        assertFalse(result);

        // Clean
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id = ?", adminId);

    }

}
