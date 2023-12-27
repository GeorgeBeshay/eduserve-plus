package edu.esp.system_entities.system_uni_objs;

public class Grade {
    String courseCode;
    int studentId;
    byte season;
    String academicYear;
    byte midtermGrade;
    byte yearWork;
    byte finalGrade;

    public Grade(String courseCode, int studentId, byte season, String academicYear, byte midtermGrade, byte yearWork, byte finalGrade) {
        this.courseCode = courseCode;
        this.studentId = studentId;
        this.season = season;
        this.academicYear = academicYear;
        this.midtermGrade = midtermGrade;
        this.yearWork = yearWork;
        this.finalGrade = finalGrade;
    }

    public Grade(){}

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public byte getSeason() {
        return season;
    }

    public void setSeason(byte season) {
        this.season = season;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public byte getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(byte midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public byte getYearWork() {
        return yearWork;
    }

    public void setYearWork(byte yearWork) {
        this.yearWork = yearWork;
    }

    public byte getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(byte finalGrade) {
        this.finalGrade = finalGrade;
    }

}
