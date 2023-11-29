package edu.esp.system_entities.system_users;

public class Admin {

    private byte adminId;
    private int adminPwHash;
    private String adminName;
    private byte creatorAdminId;

    public Admin() {}

    public Admin(byte adminId, int adminPwHash, String adminName, byte creatorAdminId) {
        this.adminId = adminId;
        this.adminPwHash = adminPwHash;
        this.adminName = adminName;
        this.creatorAdminId = creatorAdminId;
    }

    public byte getAdminId() {
        return adminId;
    }

    public void setAdminId(byte adminId) {
        this.adminId = adminId;
    }

    public int getAdminPwHash() {
        return adminPwHash;
    }

    public void setAdminPwHash(int adminPwHash) {
        this.adminPwHash = adminPwHash;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public byte getCreatorAdminId() {
        return creatorAdminId;
    }

    public void setCreatorAdminId(byte creatorAdminId) {
        this.creatorAdminId = creatorAdminId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminPwHash=" + adminPwHash +
                ", adminName='" + adminName + '\'' +
                ", creatorAdminId=" + creatorAdminId +
                '}';
    }
}
