package edu.esp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.be.EspBeApplication;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.utilities.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EspBeApplication.class)
@AutoConfigureMockMvc
public class AdminEndPointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void prepare() {
        DBFacadeImp dbFacade = new DBFacadeImp(jdbcTemplate);
        dbFacade.createAdmin(
                new Admin(
                        (byte) 99,
                        Hasher.hash("1234"),
                        "AdminForTest",
                        (byte) 1
                        )
        );
    }

    @Test
    @DisplayName("Admin Sign In - Authenticated")
    public void successfulSignIn() throws Exception {

        Admin admin = new Admin();
        admin.setAdminId((byte) 99);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", admin);
        requestMap.put("password", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestMap)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(booleanResponse);

    }

    @Test
    @DisplayName("Admin Sign In - Rejected (Wrong PW)")
    public void nonValidPWSignIn() throws Exception {

        Admin admin = new Admin();
        admin.setAdminId((byte) 99);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", admin);
        requestMap.put("password", "12344");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestMap)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(booleanResponse);
    }

    @Test
    @DisplayName("Admin Sign In - Rejected (Admin ID doesn't exist)")
    public void nonValidIDSignIn() throws Exception {

        Admin admin = new Admin();
        admin.setAdminId((byte) -100);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("admin", admin);
        requestMap.put("password", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestMap)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(booleanResponse);
    }

    @Test
    @DisplayName("Admin Add course - Accepted (Course id is unique)")
    public void validAddCourse() throws Exception {

        Course newCourse = new Course("CS55","math1",
                "bla bla",(byte) 1,(byte) 3);

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/addCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newCourse)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(booleanResponse);

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );
    }

    @Test
    @DisplayName("Admin Add course - Rejected (Course id is unique but prerequisite are not in DB)")
    public void unvalidPrereqAddCourse() throws Exception {

        Course newCourse = new Course("CS55","math1",
                "bla bla",(byte) 1,(byte) 3);

        List<String> preq = new ArrayList<>();
        preq.add("CS1");

        newCourse.setPrerequisite(preq);

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/addCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newCourse)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(booleanResponse);

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );


    }

    @Test
    @DisplayName("Admin Add course - Rejected (Course id is not unique)")
    public void unvalidAddCourseId() throws Exception {

        DBFacadeImp dbFacadeImp = new DBFacadeImp(jdbcTemplate);

        Course newCourse1 = new Course("CS55","math1",
                "bla bla",(byte) 1,(byte) 3);

        dbFacadeImp.addNewCourse(newCourse1);

        Course newCourse2 = new Course("CS55","math2",
                "bla bla",(byte) 1,(byte) 3);

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/admin-endpoint/addCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newCourse2)))
                .andReturn();

        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        boolean booleanResponse = Boolean.parseBoolean(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(booleanResponse);

        jdbcTemplate.batchUpdate("""
                DELETE FROM course WHERE course_code = 'CS55';
                """
        );


    }

    // Helper method to convert Map to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
