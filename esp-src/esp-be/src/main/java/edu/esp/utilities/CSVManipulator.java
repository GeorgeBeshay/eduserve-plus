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
    private final String csvFolderPrefix = "src/main/java/edu/esp/csv_files/";

    public List<UnregisteredInstructor> readUnregisteredInstructors(String filename){
        List<UnregisteredInstructor> instructors = new ArrayList<>();

        try (Reader reader = new FileReader(csvFolderPrefix + filename)) {
            csvIterator = new CSVParser(reader, CSVFormat.DEFAULT).iterator();

            csvIterator.next(); // Pass the header row

            int id, otpHash;
            CSVRecord record;

            while (csvIterator.hasNext()) {
                record = csvIterator.next();
                id = Integer.parseInt(record.get(0));
                otpHash = Integer.parseInt(record.get(1));
                if (record.size() > 2 || id < 0) {
                    throw new RuntimeException("Error in records inside the CSV file.");
                }
                instructors.add(new UnregisteredInstructor(id, otpHash));
            }

            return instructors;

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error reading instructor CSV file: " + e.getClass() + ": " +  e.getMessage(), 1);
            return null;
        }
    }

    public List<UnregisteredStudent> readUnregisteredStudents(String filename){
        List<UnregisteredStudent> students = new ArrayList<>();

        try (Reader reader = new FileReader(csvFolderPrefix + filename)) {
            csvIterator = new CSVParser(reader, CSVFormat.DEFAULT).iterator();

            csvIterator.next(); // Pass the header row
            int id, otpHash;
            CSVRecord record;

            while (csvIterator.hasNext()) {
                record = csvIterator.next();
                id = Integer.parseInt(record.get(0));
                otpHash = Integer.parseInt(record.get(1));
                if (record.size() > 2 || id < 0) {
                    throw new RuntimeException("Error in records inside the CSV file.");
                }
                students.add(
                        new UnregisteredStudent(id, otpHash)
                );
            }

            return students;

        } catch (Exception e) {
            Logger.logMsgFrom(this.getClass().getName(), "Error reading student CSV file: " + e.getClass() + ": " + e.getMessage(), 1);
            return null;
        }
    }

}
