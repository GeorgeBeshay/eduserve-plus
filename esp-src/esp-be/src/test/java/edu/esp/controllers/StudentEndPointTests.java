package edu.esp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.be.EspBeApplication;
import edu.esp.database.DBFacadeImp;
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

@SpringBootTest(classes = EspBeApplication.class)
@AutoConfigureMockMvc
public class StudentEndPointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        DBFacadeImp dbFacadeImp = new DBFacadeImp(jdbcTemplate);
        Student student = new Student();
        student.setStudentId(100);
        student.setStudentPwHash(1983716562);
        student.setSsn("123456789");

        dbFacadeImp.createStudent(student);
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
    @DisplayName("Student Sign In - Authenticated Wrong Password")
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
    @DisplayName("Student Sign In - Authenticated Wrong id")
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

    @AfterEach
    public void removeStudent() {
        DBFacadeImp dbFacadeImp = new DBFacadeImp(jdbcTemplate);
        dbFacadeImp.deleteStudent(100);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
