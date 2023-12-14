package edu.esp.utilities;

import edu.esp.system_entities.system_users.UnregisteredInstructor;
import edu.esp.system_entities.system_users.UnregisteredStudent;
import org.apache.commons.csv.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVManipulator {

    private Iterator<CSVRecord> csvIterator;
    public final String csvFolderPrefix = "src/main/resources/csv_files/";

    /**
     * Reads unregistered instructors from a .csv file uploaded to the system by an admin.
     * @param filename the csv file name (without the path) which is going to be parsed.
     * @return On normal completion, A list containing at least one {@code UnregisteredInstructor} entry.
     *         When an error occurs, a {@code null} value is returned.
     */
    public List<UnregisteredInstructor> readUnregisteredInstructors(String filename){
        List<UnregisteredInstructor> instructors = new ArrayList<>();

        try (Reader reader = new FileReader(csvFolderPrefix + filename)) {
            csvIterator = new CSVParser(reader, CSVFormat.DEFAULT).iterator();

            CSVRecord record; // Placeholder record for using with iterator

            // Parse the header row, check for correct size and column names
            record = csvIterator.next();
            if (record.size() != 3
                    || !record.get(0).equals("instructorID")
                    || !record.get(1).equals("instructorOTP")
                    || !record.get(2).equals("instructorDpt")) {
                throw new RuntimeException("Wrong instructor csv header format.");
            }

            // Check for presence of actual data
            if (!csvIterator.hasNext()) {
                throw new RuntimeException("Instructor csv file has a header only with no data.");
            }

            // Parse the data rows
            int id, otpHash;
            byte dpt;
            while (csvIterator.hasNext()) {
                record = csvIterator.next();
                if (record.size() != 3) {
                    throw new RuntimeException("An instructor record has an incorrect size in the csv file.");
                }
                if ((id = Integer.parseInt(record.get(0))) < 0) {
                    throw new RuntimeException("An instructor record has a negative ID in the csv file.");
                }
                otpHash = Hasher.hash(record.get(1));
                dpt = Byte.parseByte(record.get(2));
                instructors.add(new UnregisteredInstructor(id, otpHash, dpt));
            }

            return instructors;

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error reading instructor CSV file: " + e.getClass() + ": " +  e.getMessage(), 1);
            return null;
        }
    }

    /**
     * Reads unregistered students from a .csv file uploaded to the system by an admin.
     * @param filename the csv file name (without the path) which is going to be parsed.
     * @return On normal completion, A list containing at least one {@code UnregisteredStudent} entry.
     *         When an error occurs, a {@code null} value is returned.
     */
    public List<UnregisteredStudent> readUnregisteredStudents(String filename){
        List<UnregisteredStudent> students = new ArrayList<>();

        try (Reader reader = new FileReader(csvFolderPrefix + filename)) {
            csvIterator = new CSVParser(reader, CSVFormat.DEFAULT).iterator();

            CSVRecord record; // Placeholder record for using with iterator

            // Parse the header row, check for correct size and column names
            record = csvIterator.next();
            if (record.size() != 3
                    || !record.get(0).equals("studentID")
                    || !record.get(1).equals("studentOTP")
                    || !record.get(2).equals(("studentDpt"))) {
                throw new RuntimeException("Wrong student csv header format.");
            }

            // Check for presence of actual data
            if (!csvIterator.hasNext()) {
                throw new RuntimeException("Student csv file has a header only with no data.");
            }

            // Parse the data rows
            int id, otpHash;
            byte dpt;
            while (csvIterator.hasNext()) {
                record = csvIterator.next();
                if (record.size() != 3) {
                    throw new RuntimeException("A student record has an incorrect size in the csv file.");
                }
                if ((id = Integer.parseInt(record.get(0))) < 0){
                    throw new RuntimeException("A student record has a negative ID in the csv file.");
                }
                otpHash = Hasher.hash(record.get(1));
                dpt = Byte.parseByte(record.get(2));
                students.add(
                        new UnregisteredStudent(id, otpHash, dpt)
                );
            }

            return students;

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error reading student CSV file: " + e.getClass() + ": " + e.getMessage(), 1);
            return null;
        }
    }

}