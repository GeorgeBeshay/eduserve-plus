package edu.esp.database.daos;

import edu.esp.system_entities.system_uni_objs.Course;
import edu.esp.utilities.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CourseDAO extends DAO<Course> {

    public CourseDAO( JdbcTemplate jdbcTemplate ) {
        super(jdbcTemplate, Course.class);
    }


    @Transactional
    public boolean addNewCourse( Course newCourse ) {

        try {
            List<String> preqId = newCourse.getPrerequisite();
            if ( preqId!=null && !preqId.isEmpty() ) {
                boolean flag = checkPrereq( preqId );
                if ( !flag ){
                    return false;
                }
            }

            insertIntoCourseTable( newCourse );
            if ( preqId!=null && !preqId.isEmpty() ) {
                batchInsertIntoCoursePrereqTable( newCourse.getCourseCode(), preqId );
            }
        } catch (Exception e){
            return false;
        }

        return true;

    }


    private void insertIntoCourseTable( Course newCourse ){

        String sqlQuery = "INSERT INTO course (course_code, course_name, course_description," +
                " offering_dpt, credit_hrs) VALUES (?, ?, ?, ?, ?)";
        int rowAffected = this.jdbcTemplate.update(sqlQuery,newCourse.getCourseCode(),
                newCourse.getCourseName(), newCourse.getCourseDescription(),
                newCourse.getOfferingDpt(),newCourse.getCreditHrs());

    }


    private void batchInsertIntoCoursePrereqTable( String courseCode, List<String> prereq ){

        String insertQuery = "INSERT INTO course_prereq (course_code, preq_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate( insertQuery, new BatchPreparedStatementSetter() {
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

    private boolean checkPrereq( List<String> preq ){
        String sqlQuery = "SELECT course_code FROM course";
        List<String> preqList = jdbcTemplate.queryForList( sqlQuery, String.class );
        for ( String pre : preq ) {
            if ( !preqList.contains( pre ) ){
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves a list of the courses offered by the department with id 'offeringDpt'
     * @param offeringDpt The offering department identifier to search for.
     * @return A list of 'Course' objects that are being offered by a department with id 'offeringDpt',
     * OR null if:
     * 1. A negative offeringDpt was given.
     * 2. An error had occurred during query execution.
     */
    @Transactional
    public List<Course> findByOfferingDpt(Byte offeringDpt) {

        try {
            assert offeringDpt != null: "Offering department can't be null!";
            assert offeringDpt >= 0 : "Offering department can't be a negative value!";

            String sql = """
                SELECT *
                FROM course
                WHERE offering_dpt = ?
                """;

            return jdbcTemplate.query(sql, rowMapper, offeringDpt);
        }
        catch (AssertionError | Exception error) {
            Logger.logMsgFrom(this.getClass().getName(), error.getMessage(), 1);
            return null;
        }

    }

    /**
     * @return A list of courses that meet the conjunction of the following criteria:
     * <pre>1. Has the same department ID as the student</pre>
     * <pre>2. Being offered this semester</pre>
     * <pre>3. Not passed or taken before</pre>
     * <pre>4. Not already registered this semester</pre>
     * <pre>5. All its prerequisites have been passed by the student</pre>
     */
    public List<Course> getAvailableCourses(int studentId) {
        try {
            return jdbcTemplate.query("EXEC dbo.getAvailableCourses " + studentId, rowMapper);
        }
        catch (Exception error) {
            Logger.logMsgFrom(this.getClass().getName(), error.getMessage(), 1);
            return null;
        }
    }
}
