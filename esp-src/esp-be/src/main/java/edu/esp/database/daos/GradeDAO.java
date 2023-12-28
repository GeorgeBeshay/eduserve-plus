package edu.esp.database.daos;

import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_uni_objs.Grade;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class GradeDAO extends DAO<Grade> {

    public GradeDAO(JdbcTemplate jdbcTemplate){ super(jdbcTemplate, Grade.class);}


    private boolean withDrawFromCourse(int id, String courseCode, byte season, String academicYear){

        int result = jdbcTemplate.update("DELETE FROM grades" +
                " WHERE course_code = ? and student_id = ?" +
                "and season = ? and academic_year = ? and passed IS NULL;"
                ,courseCode ,id, season, academicYear);

        return result == 1;
    }

    /**
     * @param id is the student id
     * @param courses is a List<Course> that the user would like to withdraw from
     * @return integer "number of successfully withdrawal courses"
     */
    public int withdrawFromCourses(int id, List<Course> courses){
        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);
        boolean courseWithdrawAllowed = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT course_withdrawal_allowed from system_state;", boolean.class));

        if(courseWithdrawAllowed){
            Logger.logMsgFrom(this.getClass().getName(), "start with draw from database...", 0);
            int i = 0;
            for (Course course: courses){
                assert currentSeason != null;
                boolean result = withDrawFromCourse(id, course.getCourseCode(), (byte)(currentSeason.intValue()), currentYear);
                if (result)
                    i++;
            }
            return i;
        }
        else{
            Logger.logMsgFrom(this.getClass().getName(), "course withdrawal not allowed now", 1);
            return 0;
        }

    }

    public List<Grade> getAllGrades(){
        List<Grade> grades = null;
        grades = jdbcTemplate.query("SELECT * FROM grades;", rowMapper);
        return grades;
    }



}
