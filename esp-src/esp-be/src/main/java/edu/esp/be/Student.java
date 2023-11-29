package edu.esp.be;

public class Student {
    public String ID;
    public String password;
    public String newPassword;
    public String confirmNewPassword;
    public String fullName;
    public String SSN;
    public String dateOfBirth;
    public String address;
    public String phone;
    public String landline;
    public String email;
    public String gender;

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + ID + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmNewPassword='" + confirmNewPassword + '\'' +
                ", fullName='" + fullName + '\'' +
                ", SSN='" + SSN + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", landline='" + landline + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
