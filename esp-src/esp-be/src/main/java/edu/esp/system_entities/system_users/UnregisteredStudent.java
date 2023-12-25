package edu.esp.system_entities.system_users;

public class UnregisteredStudent {
    private int studentId;
    private int studentTempPwHash;
    private byte dptId;

    public UnregisteredStudent() {}

    public UnregisteredStudent(int studentId, int studentTempPwHash, byte dptId) {
        this.studentId = studentId;
        this.studentTempPwHash = studentTempPwHash;
        this.dptId = dptId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentTempPwHash() {
        return studentTempPwHash;
    }

    public void setStudentTempPwHash(int studentTempPwHash) {
        this.studentTempPwHash = studentTempPwHash;
    }

    public byte getDptId() {
        return dptId;
    }

    public void setDptId(byte dptId) {
        this.dptId = dptId;
    }
}
