package edu.esp.system_entities.system_users;

public class UnregisteredStudent {
    private int studentId;
    private int studentTempPwHash;

    public UnregisteredStudent() {}

    public UnregisteredStudent(int studentId, int studentTempPwHash) {
        this.studentId = studentId;
        this.studentTempPwHash = studentTempPwHash;
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
}
