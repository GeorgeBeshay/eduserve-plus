-- Script to add a dpt_id column for unregistered students and instructors
-- Related to ESP-75 in Milestone 2

ALTER TABLE unregistered_student ADD dpt_id TINYINT;
ALTER TABLE unregistered_student
    ADD CONSTRAINT FK_student_dpt_id FOREIGN KEY (dpt_id) REFERENCES department(dpt_id);

ALTER TABLE unregistered_instructor ADD dpt_id TINYINT;
ALTER TABLE unregistered_instructor
    ADD CONSTRAINT FK_instructor_dpt_id FOREIGN KEY (dpt_id) REFERENCES department(dpt_id);