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

    public Grade(String courseCode, int studentId, byte season, String academicYear) {
        this.courseCode = courseCode;
        this.studentId = studentId;
        this.season = season;
        this.academicYear = academicYear;
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        boolean c1 = this.studentId == ((Grade) obj).studentId;
        boolean c2 = this.courseCode.equals(((Grade) obj).courseCode);
        boolean c3 = this.academicYear.equals(((Grade) obj).academicYear);
        boolean c4 = this.season == ((Grade) obj).season;

        return c1 && c2 && c3 && c4;
    }

}
