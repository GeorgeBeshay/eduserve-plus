package edu.esp.database.daos;

import edu.esp.system_entities.system_uni_objs.Grade;
import org.springframework.jdbc.core.JdbcTemplate;

public class GradeDAO extends DAO<Grade> {

    public GradeDAO(JdbcTemplate jdbcTemplate){ super(jdbcTemplate, Grade.class);}


    public boolean withDrawFromCourse(int id, String courseCode, byte season, String academicYear){

        int result = jdbcTemplate.update("DELETE FROM grades" +
                " WHERE course_code = ? and student_id = ?" +
                "and season = ? and academic_year = ?"
                ,courseCode ,id, season, academicYear);

        return result == 1;
    }



}
