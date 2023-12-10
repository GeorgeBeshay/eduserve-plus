package edu.esp.system_entities.system_users;

public class Instructor {
    private int instructorId;
    private int instructorPwHash;
    private byte departmentId;
    private String instructorName;
    private String phone;
    private String email;
    private String officeHours;

    public Instructor() {}


    public Instructor(int instructorId, int instructorPwHash, byte departmentId, String instructorName,
                      String phone, String email, String officeHours) {
        this.instructorId = instructorId;
        this.instructorPwHash = instructorPwHash;
        this.departmentId = departmentId;
        this.instructorName = instructorName;
        this.phone = phone;
        this.email = email;
        this.officeHours = officeHours;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getInstructorPwHash() {
        return instructorPwHash;
    }

    public void setInstructorPwHash(int instructorPwHash) {
        this.instructorPwHash = instructorPwHash;
    }

    public byte getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(byte departmentId) {
        this.departmentId = departmentId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }




    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId=" + instructorId +
                ", instructorPwHash=" + instructorPwHash +
                ", departmentId=" + departmentId +
                ", instructorName='" + instructorName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", officeHours='" + officeHours + '\'' +
                '}';
    }
}
