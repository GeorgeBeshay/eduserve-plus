package edu.esp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = EspBeApplication.class)
@AutoConfigureMockMvc
public class StudentEndPointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public void setUp() {

        jdbcTemplate.batchUpdate("""
            DELETE FROM unregistered_student WHERE student_id IN (%d);
            DELETE FROM student WHERE student_id IN (%d, %d);
            """.formatted(101, 101, 100));
        jdbcTemplate.batchUpdate("""
            INSERT INTO unregistered_student (student_id, student_temp_pw_hash)
            VALUES
                (%d, %d);
            """.formatted(101, 1983716562));
        jdbcTemplate.batchUpdate("""
            INSERT INTO student (student_id, Student_pw_hash, ssn)
            VALUES
                (%d, %d, '123456789');
            """.formatted(100, 1983716562));
    }

    @Test
    @DisplayName("Student Sign In - Authenticated")
    public void successfulSignIn() throws Exception {

        Student student = new Student();
        student.setStudentId(100);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signIn")
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
    @DisplayName("Student Sign In - Rejected Wrong Password")
    public void wrongPasswordSignIn() throws Exception {

        Student student = new Student();
        student.setStudentId(100);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "123456");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signIn")
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
    @DisplayName("Student Sign In - Rejected Wrong id")
    public void signInWithWrongID() throws Exception {

        Student student = new Student();
        student.setStudentId(-1);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signIn")
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
    @DisplayName("Student Sign Up - Authenticated correct")
    public void signUpValid() throws Exception {

        Student student = new Student();
        student.setStudentId(101);
        student.setSsn("ssn123456");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "567");
        requestMap.put("OTPPassword", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signUp")
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
    @DisplayName("Student Sign Up - Rejected wrong id")
    public void signUpWrongID() throws Exception {

        Student student = new Student();
        student.setStudentId(-1);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "567");
        requestMap.put("OTPPassword", "1234");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signUp")
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
    @DisplayName("Student Sign Up - Rejected wrong OTP password")
    public void signUpWrongOTPPassword() throws Exception {

        Student student = new Student();
        student.setStudentId(101);
        student.setSsn("ssn123456");

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("student", student);
        requestMap.put("password", "567");
        requestMap.put("OTPPassword", "5123");

        MvcResult result = this.mockMvc.perform(post("http://localhost:8081/esp-server/student-endpoint/signUp")
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

    @AfterAll
    public void removeStudent() {
        jdbcTemplate.batchUpdate("""
            DELETE FROM unregistered_student WHERE student_id IN (%d);
            DELETE FROM student WHERE student_id IN (%d, %d);
            """.formatted(101, 101, 100));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
