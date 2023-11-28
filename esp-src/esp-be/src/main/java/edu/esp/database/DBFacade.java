package edu.esp.database;

import edu.esp.system_entities.system_users.Admin;
import edu.esp.system_entities.system_users.Instructor;
import edu.esp.system_entities.system_users.Student;

public interface DBFacade {
    // will be refactored
    Student loadStudentData(int studentId);
    Instructor loadInstructorData(int instructorId);
    Admin loadAdmin(int adminId);

//    void addStudent(Student unregisteredStudent);
//    void addAdmin(Admin unregisteredAdmin);
//    void addInstructor(Instructor unregisteredInstructor);
//    void updateCoursePrerequisites(String courseId, String prerequisiteId);
//    void updateCourses(Object course);
//    void updateCourses(int courseId, Object newCourse);
//    void updateDepartments(Object department);
//    void updateDepartments(String departmentId, Object newDepartment);
//    void updateGradingCriteria(Object gradingCriteria);
//    void addWarning(Object warning);
//    void updateTicket(Object ticket);
}
