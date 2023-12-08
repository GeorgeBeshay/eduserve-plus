package edu.esp.database.daos;

import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.system_entities.system_users.Admin;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CourseDAO {

    private final JdbcTemplate jdbcTemplate;

    public CourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public boolean addNewCourse(Course newCourse) {
        try {
            List<String> preqId = newCourse.getPrerequisite();
            if(preqId!=null && !preqId.isEmpty()) {
                boolean flag = checkPrereq(preqId);
                if (!flag){
                    return false;
                }
            }

            insertIntoCourseTable(newCourse);
            if(preqId!=null && !preqId.isEmpty()) {
                batchInsertIntoCoursePrereqTable(newCourse.getCourseCode(), preqId);
            }
        } catch (Exception e){
            return false;
        }

        return true;

    }


    private void insertIntoCourseTable(Course newCourse){

        String sqlQuery = "INSERT INTO course (course_code, course_name, course_description," +
                " offering_dpt, credit_hrs) VALUES (?, ?, ?, ?, ?)";
        int rowAffected = this.jdbcTemplate.update(sqlQuery,newCourse.getCourseCode(),
                newCourse.getCourseName(), newCourse.getCourseDescription(),
                newCourse.getOfferingDpt(),newCourse.getCreditHrs());

    }


    private void batchInsertIntoCoursePrereqTable(String courseCode, List<String> prereq){

        String insertQuery = "INSERT INTO course_prereq (course_code, preq_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String prereqElement = prereq.get(i);
                preparedStatement.setString(1, courseCode);
                preparedStatement.setString(2, prereqElement);
            }

            @Override
            public int getBatchSize() {
                return prereq.size();
            }
        });

    }

    private boolean checkPrereq(List<String> preq){
        String sqlQuery = "SELECT course_code FROM course";
        List<String> preqList = jdbcTemplate.queryForList(sqlQuery, String.class);
        for (String pre:preq) {
            if(!preqList.contains(pre)){
                return false;
            }
        }
        return true;
    }
}
