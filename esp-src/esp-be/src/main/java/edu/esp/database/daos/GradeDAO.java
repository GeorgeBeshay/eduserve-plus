package edu.esp.database.daos;

import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_uni_objs.Grade;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GradeDAO extends DAO<Grade> {

    public GradeDAO(JdbcTemplate jdbcTemplate){ super(jdbcTemplate, Grade.class);}


    private boolean withDrawFromCourse(int id, String courseCode, byte season, String academicYear){

        int result = jdbcTemplate.update("DELETE FROM grades" +
                " WHERE course_code = ? and student_id = ?" +
                "and season = ? and academic_year = ?"
                ,courseCode ,id, season, academicYear);

        return result == 1;
    }

    /**
     * @param id is the student id
     * @param courses is a List<Course> that the user would like to withdraw from
     * @return integer "number of successfully withdraw courses"
     */
    public int withdrawFromCourses(int id, List<Course> courses){
        Integer currentSeason = jdbcTemplate.queryForObject("SELECT current_season from system_state;", Integer.class);
        String currentYear = jdbcTemplate.queryForObject("SELECT current_academic_year from system_state;", String.class);
        int i = 0;
        for (Course course: courses){
            assert currentSeason != null;
            boolean result = withDrawFromCourse(id, course.getCourseCode(), (byte)(currentSeason.intValue()), currentYear);
            if (result)
                i++;
        }
        return i;
    }



}
