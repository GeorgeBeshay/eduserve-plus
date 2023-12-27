-- Function to return whether the given student has passed the given course's prerequisites or not

CREATE FUNCTION finishedPrerequisites(@studentId INT, @courseCode VARCHAR(7))
RETURNS BIT
AS
BEGIN
	-- Get course prerequisites
	DECLARE @coursePrereqs TABLE(prereqCode VARCHAR(7));
	INSERT INTO @coursePrereqs(prereqCode) SELECT preq_id FROM course_prereq WHERE course_code = @courseCode;
	-- Get the count of courses the student has passed from the prerequisites
	DECLARE @takenCount INT = (
		SELECT COUNT(*)
		FROM grades
		WHERE student_id = @studentId AND course_code IN (SELECT * FROM @coursePrereqs) AND passed = 1
	);
	-- Check if the passed count is equal to the course prerequisite count or not
	IF @takenCount = (SELECT COUNT(*) FROM @coursePrereqs)
		RETURN 1;
	RETURN 0;
END;


-- Stored procedure to get the available courses for a given student in the current semester

CREATE PROCEDURE getAvailableCourses(@studentId INT)
AS
BEGIN
	SET NOCOUNT ON
	-- Get the student's department ID
	DECLARE @dptId TINYINT = (SELECT dpt_id FROM student WHERE student_id = @studentId);
	-- Get the current semester
	DECLARE @currentSeason TINYINT = (SELECT current_season from system_state);
	DECLARE @currentYear CHAR(4) = (SELECT current_academic_year from system_state);
	-- Get the available courses this semester offered by the student's department
	DECLARE @availableCourses TABLE(courseCode VARCHAR(7));
	INSERT INTO @availableCourses
		SELECT instructs.course_code
		FROM instructs
		JOIN course ON instructs.course_code = course.course_code
		WHERE academic_year = @currentYear AND season = @currentSeason AND offering_dpt = @dptId;
	-- Get the available courses which the student has passed or has already registered this semester
	DECLARE @passedOrRegisteredCourses TABLE(courseCode VARCHAR(7));
	INSERT INTO @passedOrRegisteredCourses
		SELECT courseCode
		FROM grades JOIN @availableCourses ON course_code = courseCode
		WHERE (student_id = @studentId AND passed = 1) OR (season = @currentSeason AND academic_year = @currentYear);
	-- Select the available courses whose prerequisites the student has passed but the student has not taken yet
	SELECT *
	FROM course
	WHERE course_code IN (
		SELECT *
		FROM @availableCourses
		WHERE dbo.finishedPrerequisites(@studentId, courseCode) = 1 AND courseCode NOT IN (SELECT * FROM @passedOrRegisteredCourses)
	);
END;