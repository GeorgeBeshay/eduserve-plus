package edu.esp.system_entities.system_users;

public class UnregisteredInstructor {
    private int instructor_id;
    private int Instructor_temp_pw_hash;

    public UnregisteredInstructor(int instructor_id, int Instructor_temp_pw_hash) {
        this.instructor_id = instructor_id;
        this.Instructor_temp_pw_hash = Instructor_temp_pw_hash;
    }

    public int getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id = instructor_id;
    }

    public int getInstructor_temp_pw_hash() {
        return Instructor_temp_pw_hash;
    }

    public void setInstructor_temp_pw_hash(int Instructor_temp_pw_hash) {
        this.Instructor_temp_pw_hash = Instructor_temp_pw_hash;
    }

    @Override
    public String toString() {
        return "UnregisteredInstructor{" +
                "instructor_id=" + instructor_id +
                ", Instructor_temp_pw_hash=" + Instructor_temp_pw_hash +
                '}';
    }
}

