package edu.esp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.esp.be.EspBeApplication;
import edu.esp.database.DBFacadeImp;
import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Admin;
import edu.esp.utilities.CSVManipulator;
import edu.esp.utilities.Hasher;
import org.junit.jupiter.api.AfterEach;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = EspBeApplication.class)
@AutoConfigureMockMvc
public class AdminEndPointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void prepare() {
        // TODO insert testing departments
        DBFacadeImp dbFacade = new DBFacadeImp(jdbcTemplate);
        dbFacade.createAdmin(
                new Admin(
                        (byte) 99,
                        Hasher.hash("1234"),
                        "AdminForTest",
                        (byte) 0
                        )
        );
    }

    @AfterEach
    public void close() {
        jdbcTemplate.update("DELETE FROM sys_admin WHERE admin_id = 99;");
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

        Course newCourse = new Course("TEST1","math1",
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

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST1';");
    }

    @Test
    @DisplayName("Admin Add course - Rejected (Course id is unique but prerequisite are not in DB)")
    public void unvalidPrereqAddCourse() throws Exception {

        Course newCourse = new Course("TEST2","math1",
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

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST2';");

    }

    @Test
    @DisplayName("Admin Add course - Rejected (Course id is not unique)")
    public void unvalidAddCourseId() throws Exception {

        DBFacadeImp dbFacadeImp = new DBFacadeImp(jdbcTemplate);

        Course newCourse1 = new Course("TEST3","math1",
                "bla bla",(byte) 1,(byte) 3);

        dbFacadeImp.addNewCourse(newCourse1);

        Course newCourse2 = new Course("TEST3","math2",
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

        jdbcTemplate.update("DELETE FROM course WHERE course_code = 'TEST3';");

    }

    @Test
    @DisplayName("Admin Add unregistered students - Accepted")
    public void addUnregisteredStudents() throws Exception {

        jdbcTemplate.update("DELETE FROM unregistered_student WHERE student_id IN(50, 51, 52);");

        CSVManipulator csvManipulator = new CSVManipulator();
        String originalFilename = "validUnregisteredStudents.csv";
        Path path = Paths.get(csvManipulator.csvFolderPrefix + originalFilename);
        Files.createFile(path);

        String csvContent = """
                        studentID,studentOTP,studentDpt
                        50,13,1
                        51,125,1
                        52,1245,1""";

        BufferedWriter writer = new BufferedWriter(new FileWriter(csvManipulator.csvFolderPrefix + originalFilename));
        writer.write(csvContent);
        writer.close();

        MockMultipartFile unregisteredStudents = new MockMultipartFile("unregisteredStudents", originalFilename, "text/csv", Files.readAllBytes(path));


        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8081/esp-server/admin-endpoint/addUnregisteredStudents")
                        .file(unregisteredStudents)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn();


        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        int addedStudents = Integer.parseInt(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(3, addedStudents);

        jdbcTemplate.update("DELETE FROM unregistered_student WHERE student_id IN(50, 51, 52);");
        Files.delete(Paths.get(csvManipulator.csvFolderPrefix + originalFilename));
    }


    @Test
    @DisplayName("Admin Add unregistered students - Rejected because of sending null multipart file")
    public void addUnregisteredStudentsNull() throws Exception {

        MockMultipartFile unregisteredStudents = new MockMultipartFile("unregisteredStudents", "", "text/csv", (byte[]) null);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.multipart("http://localhost:8081/esp-server/admin-endpoint/addUnregisteredStudents")
                        .file(unregisteredStudents)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn();


        // Retrieve the response status code
        int status = result.getResponse().getStatus();

        // Retrieve the response content
        String content = result.getResponse().getContentAsString();
        int addedStudents = Integer.parseInt(content);

        // Assert the status code and the boolean value
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals(0, addedStudents);

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
