--added a trigger so that whenever a new instructor signs up, his record gets deleted from the unregistered_instructor table
CREATE TRIGGER DeleteCorrespondingTuple
ON instructor
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Assuming that 'ID' is the common attribute between the two tables
    -- You may need to adjust the column names based on your actual schema
    DELETE FROM unregistered_instructor
    WHERE instructor_id IN (SELECT instructor_id FROM INSERTED);
END;