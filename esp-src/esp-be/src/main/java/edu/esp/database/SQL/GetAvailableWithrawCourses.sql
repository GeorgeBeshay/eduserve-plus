CREATE PROCEDURE getAvailableWithdrawCourses(@studentId INT)
AS
BEGIN
    SET NOCOUNT ON
    DECLARE @currentSeason TINYINT = (SELECT current_season from system_state);
	DECLARE @currentYear CHAR(4) = (SELECT current_academic_year from system_state);

    DECLARE @courseCodes TABLE (course_code varchar(7));

    INSERT INTO @courseCodes
        SELECT grades.course_code
        FROM grades
        WHERE student_id = @studentId AND season = @currentSeason AND academic_year = @currentYear AND passed IS NULL;

    SELECT *
    FROM course
    WHERE course.course_code IN (SELECT course_code FROM @courseCodes);

END;