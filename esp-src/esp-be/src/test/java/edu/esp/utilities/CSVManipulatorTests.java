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

    String csvRoot;
    BufferedWriter writer;
    CSVManipulator manipulator;
    String filename;


    @BeforeEach
    public void setup() {
        manipulator = new CSVManipulator();
        csvRoot = manipulator.csvFolderPrefix;
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

    // ============================================= INSTRUCTOR CSV TESTS =============================================

    @Test
    @DisplayName("CSVManipulator - Reading valid instructors")
    public void testReadValidInstructorFile() throws IOException {
        writeFile("validInstructors.csv",
                """
                        instructorID,instructorOTP
                        1001,bl0550m
                        2002,bu6613s
                        3003,6utt3rcup"""
        );

        List<UnregisteredInstructor> list = manipulator.readUnregisteredInstructors(filename);
        int[] ids = {1001,2002,3003};
        String[] OTPs = {"bl0550m","bu6613s","6utt3rcup"};

        assertEquals(3, list.size());
        for (int i=0 ; i<3 ; i++) {
            assertEquals( ids[i] , list.get(i).getInstructorId() );
            assertEquals( Hasher.hash(OTPs[i]) , list.get(i).getInstructorTempPwHash());
        }
    }

    @Test
    @DisplayName("CSVManipulator - Reading invalid format instructors")
    public void testReadInvalidFormatInstructorFile() throws IOException {
        writeFile("invalidFormatInstructors.csv",
                """
                        instructorID,instructorOTP
                        1001,bl0550m
                        2002
                        3003,6utt3rcup"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading negative id instructors")
    public void testReadInvalidIdInstructorFile() throws IOException {
        writeFile("invalidIdInstructors.csv",
                """
                        instructorID,instructorOTP
                        1001,bl0550m
                        2002,bu6613s
                        -303,6utt3rcup"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading instructor file with a header only")
    public void testReadInstructorFileHeaderOnly() throws IOException {
        writeFile("headerOnlyInstructor.csv", "instructorID,instructorOTP");

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading instructor file with an invalid header format")
    public void testReadInvalidHeaderInstructorFile() throws IOException {
        writeFile("invalidHeaderInstructors.csv",
                """
                        studentID,studentOTP
                        1001,bl0550m
                        2002,bu6613s
                        303,6utt3rcup"""
        );

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading empty instructor file")
    public void testReadEmptyInstructorFile() throws IOException {
        writeFile("emptyInstructors.csv", "\t\n\t\t\n");

        assertNull(manipulator.readUnregisteredInstructors(filename));
    }

    // ============================================= STUDENT CSV TESTS =============================================

    @Test
    @DisplayName("CSVManipulator - Reading valid students")
    public void testReadValidStudentFile() throws IOException {
        writeFile("validStudents.csv",
                """
                        studentID,studentOTP
                        1010,s4m33r
                        2020,sh4h33r
                        3030,64h33r"""
        );

        List<UnregisteredStudent> list = manipulator.readUnregisteredStudents(filename);
        int[] ids = {1010,2020,3030};
        String[] OTPs = {"s4m33r","sh4h33r","64h33r"};

        assertEquals(3, list.size());
        for (int i=0 ; i<3 ; i++) {
            assertEquals( ids[i] , list.get(i).getStudentId() );
            assertEquals( Hasher.hash(OTPs[i]) , list.get(i).getStudentTempPwHash());
        }
    }

    @Test
    @DisplayName("CSVManipulator - Reading invalid format students")
    public void testReadInvalidFormatStudentFile() throws IOException {
        writeFile("invalidFormatStudents.csv",
                """
                        studentID,studentOTP
                        haHAA,s4m33r
                        2020,sh4h33r
                        3030,64h33r"""
        );

        assertNull(manipulator.readUnregisteredStudents(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading negative id students")
    public void testReadInvalidIdStudentFile() throws IOException {
        writeFile("invalidIdStudents.csv",
                """
                        studentID,studentOTP
                        1010,s4m33r
                        -100,sh4h33r
                        3030,64h33r"""
        );

        assertNull(manipulator.readUnregisteredStudents(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading student file with a header only")
    public void testReadStudentFileHeaderOnly() throws IOException {
        writeFile("headerOnlyStudents.csv", "studentID,studentOTP");

        assertNull(manipulator.readUnregisteredStudents(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading student file with an invalid header format")
    public void testReadInvalidHeaderStudentFile() throws IOException {
        writeFile("invalidHeaderStudents.csv",
                """
                        instructorID,instructorOTP
                        1010,s4m33r
                        100,sh4h33r
                        3030,64h33r"""
        );

        assertNull(manipulator.readUnregisteredStudents(filename));
    }

    @Test
    @DisplayName("CSVManipulator - Reading empty student file")
    public void testEmptyStudentFile() throws IOException {
        writeFile("emptyStudents.csv", "\t\t\t\n\n\t");

        assertNull(manipulator.readUnregisteredStudents(filename));
    }
}
