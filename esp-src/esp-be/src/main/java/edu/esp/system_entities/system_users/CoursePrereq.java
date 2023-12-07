package edu.esp.system_entities.system_users;

public class CoursePrereq {
    String courseCode;
    String preqId;

    public CoursePrereq(String courseCode, String preqId) {
        this.courseCode = courseCode;
        this.preqId = preqId;
    }

    @Override
    public String toString() {
        return "CoursePrereq{" +
                "courseCode='" + courseCode + '\'' +
                ", preqId='" + preqId + '\'' +
                '}';
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getPreqId() {
        return preqId;
    }

    public void setPreqId(String preqId) {
        this.preqId = preqId;
    }
}
