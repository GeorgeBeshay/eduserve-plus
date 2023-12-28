package edu.esp.services;

import edu.esp.be.EspBeApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
public class StudentServicesTests {

    private final JdbcTemplate jdbcTemplate;
    private StudentServices studentServices;

    @Autowired
    public StudentServicesTests(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentServices = new StudentServices(jdbcTemplate);
    }

    @BeforeEach
    public void setUpBeforeEach() {
        this.studentServices = new StudentServices(jdbcTemplate);
    }

}
