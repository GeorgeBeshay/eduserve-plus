-- Insert into sys_admin table

INSERT INTO sys_admin (admin_id, admin_pw_hash, admin_name, creator_admin_id)
VALUES
  (1, 123456, 'Admin1', 1),
  (2, 654321, 'Admin2', 1),
  (3, 987654, 'Admin3', 2);

-- Insert into department table

INSERT INTO department (dpt_id, dpt_name)
VALUES
  (1, 'Computer Science'),
  (2, 'Physics'),
  (3, 'Mathematics');

-- Insert into instructor table

INSERT INTO instructor (instructor_id, Instructor_pw_hash, dpt_id, instructor_name, phone, email, office_hrs)
VALUES
  (1, 135790, 1, 'Prof. Anderson', '555-4321', 'prof.anderson@example.com', 'Monday 10am-12pm, Wednesday 2pm-4pm'),
  (2, 246801, 2, 'Dr. Thompson', '555-6789', 'dr.thompson@example.com', 'Tuesday 9am-11am, Thursday 1pm-3pm'),
  (3, 987123, 3, 'Prof. Williams', '555-9876', 'prof.williams@example.com', 'Friday 3pm-5pm, Saturday 10am-12pm');


-- Insert into student table

INSERT INTO student (student_id, Student_pw_hash, dpt_id, student_level, gpa, student_name, ssn, bdate, student_address, phone, landline, gender, email)
VALUES
  (1, 456789, 1, 1, 3.5, 'John Doe', '12345678901234', '2000-01-01', '123 Main St', '555-1234', '123-4567', 1, 'john.doe@example.com'),
  (2, 987654, 2, 2, 3.2, 'Jane Smith', '98765432101234', '1999-02-15', '456 Oak St', '555-5678', '234-5678', 0, 'jane.smith@example.com'),
  (3, 654321, 3, 3, 3.9, 'Bob Johnson', '11112222333344', '1998-05-20', '789 Pine St', '555-8765', '345-6789', 1, 'bob.johnson@example.com');
