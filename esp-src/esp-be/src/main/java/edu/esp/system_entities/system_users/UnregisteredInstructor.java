package edu.esp.system_entities.system_users;

public class UnregisteredInstructor {
    private int instructorID;
    private int instructorOTP;

    public UnregisteredInstructor(int InstructorID, int InstructorOTP) {
        this.instructorID = InstructorID;
        this.instructorOTP = InstructorOTP;
    }

    public int getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public int getInstructorOTP() {
        return instructorOTP;
    }

    public void setInstructorOTP(int Instructor_temp_pw_hash) {
        this.instructorOTP = Instructor_temp_pw_hash;
    }

    @Override
    public String toString() {
        return "UnregisteredInstructor{" +
                "instructor_id=" + instructorID +
                ", Instructor_temp_pw_hash=" + instructorOTP +
                '}';
    }
}

