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