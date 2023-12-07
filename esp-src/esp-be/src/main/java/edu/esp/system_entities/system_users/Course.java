package edu.esp.system_entities.system_users;

public class Course {
    private byte courseCode;
    private String courseName;
    private String courseDescription;
    private byte offeringDpt;
    private byte creditHrs;

    public Course(byte courseCode, String courseName, String courseDescription, byte offeringDpt, byte creditHrs) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.offeringDpt = offeringDpt;
        this.creditHrs = creditHrs;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCode=" + courseCode +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", offeringDpt=" + offeringDpt +
                ", creditHrs=" + creditHrs +
                '}';
    }

    public byte getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(byte courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public byte getOfferingDpt() {
        return offeringDpt;
    }

    public void setOfferingDpt(byte offeringDpt) {
        this.offeringDpt = offeringDpt;
    }

    public byte getCreditHrs() {
        return creditHrs;
    }

    public void setCreditHrs(byte creditHrs) {
        this.creditHrs = creditHrs;
    }
}
