package edu.esp.system_entities.system_uni_objs;

import java.util.List;

public class Course {
    private String courseCode;
    private String courseName;
    private String courseDescription;
    private byte offeringDpt;
    private byte creditHrs;
    private List<String> prerequisite;

    public Course(String courseCode, String courseName, String courseDescription, byte offeringDpt, byte creditHrs) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.offeringDpt = offeringDpt;
        this.creditHrs = creditHrs;
    }

    public Course() {}

    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", offeringDpt=" + offeringDpt +
                ", creditHrs=" + creditHrs +
                ", prerequisite=" + prerequisite +
                '}';
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
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

    public List<String> getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(List<String> prerequisite) {
        this.prerequisite = prerequisite;
    }
}
