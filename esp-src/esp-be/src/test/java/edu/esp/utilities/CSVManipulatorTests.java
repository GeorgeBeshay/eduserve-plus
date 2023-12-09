package edu.esp.utilities;

import edu.esp.be.EspBeApplication;
import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.system_entities.system_users.UnregisteredStudent;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest(classes = EspBeApplication.class)
public class CSVManipulatorTests {

    private final String csvRoot = "src/main/java/edu/esp/csv_files/";
    BufferedWriter writer;
    CSVManipulator manipulator;
    String filename;

    @BeforeEach
    public void setup() {
        manipulator = new CSVManipulator();
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.delete(Paths.get(csvRoot + filename));
    }

    // Method to encapsulate creating a file and writing to it
    private void writeFile(String file, String content) throws IOException {
        filename = file;
        Files.createFile(Paths.get(csvRoot + filename));
        writer = new BufferedWriter(new FileWriter(csvRoot + filename));
        writer.write(content);
        writer.close();
    }

    @Test
    @DisplayName("CSVManipulator - Reading valid instructors")
    public void testReadValidInstructorFile() throws IOException {
        writeFile("validInstructors.csv",
                """
                        InstructorID,OTPHash
                        1001,1234
                        2002,789
                        3003,-456"""
        );

        List<UnregisteredInstructor> list = manipulator.readUnregisteredInstructors(filename);
        int[] ids = {1001,2002,3003};
        int[] OTPs = {1234,789,-456};

        assertEquals(3, list.size());
        for (int i=0 ; i<3 ; i++) {
            assertEquals( ids[i] , list.get(i).getInstructor_id() );
            assertEquals( OTPs[i] , list.get(i).getInstructor_temp_pw_hash());
        }
    }

    @Test
    @DisplayName("CSVManipulator - Reading invalid format instructors")
    public void testReadInvalidFormatInstructorFile() throws IOException {
        writeFile("invalidFormatInstructors.csv",
                """
                        InstructorID,OTPHash
                        1001,1234
                        2002
                        3003,-456"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading negative id instructors")
    public void testReadInvalidIdInstructorFile() throws IOException {
        writeFile("invalidIdInstructors.csv",
                """
                        InstructorID,OTPHash
                        1001,1234
                        2002,-789
                        -303,456"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading valid students")
    public void testReadValidStudentFile() throws IOException {
        writeFile("validStudents.csv",
                """
                        StudentID,OTPHash
                        1010,-1204
                        2020,987
                        3030,456"""
        );

        List<UnregisteredStudent> list = manipulator.readUnregisteredStudents(filename);
        int[] ids = {1010,2020,3030};
        int[] OTPs = {-1204,987,456};

        assertEquals(3, list.size());
        for (int i=0 ; i<3 ; i++) {
            assertEquals( ids[i] , list.get(i).getStudentId() );
            assertEquals( OTPs[i] , list.get(i).getStudentTempPwHash());
        }
    }

    @Test
    @DisplayName("CSVManipulator - Reading invalid format students")
    public void testReadInvalidFormatStudentFile() throws IOException {
        writeFile("invalidFormatStudents.csv",
                """
                        StudentID,OTPHash
                        haHAA,-1204
                        2020,987
                        3030,456"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading negative id students")
    public void testReadInvalidIdStudentFile() throws IOException {
        writeFile("invalidIdStudents.csv",
                """
                        StudentID,OTPHash
                        1010,-1204
                        -100,987
                        3030,456"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }
}
