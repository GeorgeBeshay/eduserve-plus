package edu.esp.system_entities.system_users;

public class Student {
    private int studentId;
    private int studentPwHash;
    private byte dptId;
    private byte studentLevel;
    private float gpa;
    private String studentName;
    private String ssn;
    private String bdate;
    private String studentAddress;
    private String phone;
    private String landline;
    private boolean gender;
    private String email;

    public Student() {}

    public Student(int studentId, int studentPwHash, byte dptId, byte studentLevel, float gpa,
                   String studentName, String ssn, String bdate, String studentAddress, String phone,
                   String landline, boolean gender, String email) {
        this.studentId = studentId;
        this.studentPwHash = studentPwHash;
        this.dptId = dptId;
        this.studentLevel = studentLevel;
        this.gpa = gpa;
        this.studentName = studentName;
        this.ssn = ssn;
        this.bdate = bdate;
        this.studentAddress = studentAddress;
        this.phone = phone;
        this.landline = landline;
        this.gender = gender;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentPwHash() {
        return studentPwHash;
    }

    public void setStudentPwHash(int studentPwHash) {
        this.studentPwHash = studentPwHash;
    }

    public byte getDptId() {
        return dptId;
    }

    public void setDptId(byte dptId) {
        this.dptId = dptId;
    }

    public byte getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(byte studentLevel) {
        this.studentLevel = studentLevel;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentPwHash=" + studentPwHash +
                ", departmentId=" + dptId +
                ", studentLevel=" + studentLevel +
                ", gpa=" + gpa +
                ", studentName='" + studentName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", birthdate=" + bdate +
                ", studentAddress='" + studentAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", landline='" + landline + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                '}';
    }
}
