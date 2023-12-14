package edu.esp.system_entities.system_users;

public class Instructor {
    private int instructorId;
    private int instructorPwHash;
    private byte dptId;
    private String instructorName;
    private String phone;
    private String email;
    private String officeHrs;

    public Instructor() {}


    public Instructor(int instructorId, int instructorPwHash, byte dptId, String instructorName,
                      String phone, String email, String officeHrs) {
        this.instructorId = instructorId;
        this.instructorPwHash = instructorPwHash;
        this.dptId = dptId;
        this.instructorName = instructorName;
        this.phone = phone;
        this.email = email;
        this.officeHrs = officeHrs;
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

    public byte getDptId() {
        return dptId;
    }

    public void setDptId(byte dptId) {
        this.dptId = dptId;
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

    public String getOfficeHrs() {
        return officeHrs;
    }

    public void setOfficeHrs(String officeHrs) {
        this.officeHrs = officeHrs;
    }




    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId=" + instructorId +
                ", instructorPwHash=" + instructorPwHash +
                ", departmentId=" + dptId +
                ", instructorName='" + instructorName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", officeHours='" + officeHrs + '\'' +
                '}';
    }
}
