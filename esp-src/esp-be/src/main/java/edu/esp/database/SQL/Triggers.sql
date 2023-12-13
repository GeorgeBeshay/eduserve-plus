------------------------------------------------- INSTRUCTOR -------------------------------------------------

-- Trigger so that whenever a new instructor signs up, his record gets deleted from the unregistered_instructor table
CREATE TRIGGER tr_instructor_remove_unregistered
ON instructor
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM unregistered_instructor
    WHERE instructor_id IN (SELECT instructor_id FROM INSERTED);
END;

------------------------------------------------- STUDENT -------------------------------------------------

-- Trigger so that whenever a new student signs up, his record gets deleted from the unregistered_student table
CREATE TRIGGER tr_student_remove_unregistered
ON student
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM unregistered_student
    WHERE student_id IN (SELECT student_id FROM INSERTED);
END;

------------------------------------------------- ADMIN -------------------------------------------------

-- Trigger to set created admins' creator admin field to 0 on deletion
CREATE TRIGGER tr_adjust_created_admins
ON sys_admin
INSTEAD OF DELETE
AS
BEGIN
	SET NOCOUNT ON;
	UPDATE sys_admin
	SET creator_admin_id = 0
	WHERE creator_admin_id IN (SELECT admin_id FROM DELETED);

	DELETE FROM sys_admin WHERE admin_id IN (SELECT admin_id FROM DELETED);
END;


------------------------------------------------- COURSE -------------------------------------------------

-- Trigger to automatically remove entries from prerequisite
-- table where the deleted course depends on other courses
CREATE TRIGGER tr_delete_course_prerequisites
ON course
INSTEAD OF DELETE
AS
BEGIN
	SET NOCOUNT ON;
	DELETE FROM course_prereq
	WHERE course_code IN (SELECT course_code FROM DELETED);

	DELETE FROM course WHERE course_code IN (SELECT course_code FROM DELETED);
END;