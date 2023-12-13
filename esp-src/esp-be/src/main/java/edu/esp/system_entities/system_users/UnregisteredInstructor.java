package edu.esp.system_entities.system_users;

public class UnregisteredInstructor {
    private int instructorId;
    private int instructorTempPwHash;
    private byte dptId;

    public UnregisteredInstructor(){}

    public UnregisteredInstructor(int instructorId, int instructorTempPwHash, byte dptId) {
        this.instructorId = instructorId;
        this.instructorTempPwHash = instructorTempPwHash;
        this.dptId = dptId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getInstructorTempPwHash() {
        return instructorTempPwHash;
    }

    public void setInstructorTempPwHash(int Instructor_temp_pw_hash) {
        this.instructorTempPwHash = Instructor_temp_pw_hash;
    }

    public byte getDptId() {
        return dptId;
    }

    public void setDptId(byte dptId) {
        this.dptId = dptId;
    }

    @Override
    public String toString() {
        return "UnregisteredInstructor{" +
                "instructor_id=" + instructorId +
                ", Instructor_temp_pw_hash=" + instructorTempPwHash +
                '}';
    }
}

