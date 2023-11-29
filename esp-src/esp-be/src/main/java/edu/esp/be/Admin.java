package edu.esp.be;

public class Admin {
    public String ID;
    public String password;
    public String newPassword;

    @Override
    public String toString() {
        return "Admin{" +
                "ID='" + ID + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmNewPassword='" + confirmNewPassword + '\'' +
                '}';
    }

    public String confirmNewPassword;
}

