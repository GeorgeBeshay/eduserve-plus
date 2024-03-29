package edu.esp.system_entities.system_users;

import java.util.*;

public class Student {
    private int studentId;
    private int studentPwHash;
    private byte departmentId;
    private byte studentLevel;
    private float gpa;
    private String studentName;
    private String ssn;
    private Date birthDate;
    private String studentAddress;
    private String phone;
    private String landline;
    private boolean gender;
    private String email;

    public Student() {}

    public Student(int studentId, int studentPwHash, byte departmentId, byte studentLevel, float gpa,
                   String studentName, String ssn, Date birthDate, String studentAddress, String phone,
                   String landline, boolean gender, String email) {
        this.studentId = studentId;
        this.studentPwHash = studentPwHash;
        this.departmentId = departmentId;
        this.studentLevel = studentLevel;
        this.gpa = gpa;
        this.studentName = studentName;
        this.ssn = ssn;
        this.birthDate = birthDate;
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

    public byte getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(byte departmentId) {
        this.departmentId = departmentId;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public boolean isGender() {
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
                ", departmentId=" + departmentId +
                ", studentLevel=" + studentLevel +
                ", gpa=" + gpa +
                ", studentName='" + studentName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", birthDate=" + birthDate +
                ", studentAddress='" + studentAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", landline='" + landline + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                '}';
    }
}
