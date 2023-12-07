
USE EduServePlusDB;

CREATE TABLE sys_admin (
    admin_id TINYINT,
    admin_pw_hash INT,
    admin_name VARCHAR(60),
    creator_admin_id TINYINT,
    PRIMARY KEY (admin_id),
    FOREIGN KEY (creator_admin_id) REFERENCES sys_admin(admin_id)
);

CREATE TABLE department (
    dpt_id TINYINT,
    dpt_name VARCHAR(40),
    head_instructor_id INT,
    PRIMARY KEY (dpt_id),
    -- FOREIGN KEY (head_instructor_id) REFERENCES instructor(instructor_id)
);

CREATE TABLE unregistered_student (
    student_id INT CHECK (student_id >= 0), -- Will have to be unique with student(student_id)
    student_temp_pw_hash INT,
    PRIMARY KEY (student_id)
);

CREATE TABLE student (
    student_id INT CHECK (student_id >= 0), -- Will have to be unique with unregistered_student(student_id)
    Student_pw_hash INT,
    dpt_id TINYINT,
    student_level TINYINT CHECK (student_level BETWEEN 0 AND 4),
    gpa FLOAT CHECK (gpa BETWEEN 0.0 AND 4.0),
    student_name VARCHAR(60),
    ssn CHAR(14) UNIQUE, -- REGEX NUMBERS ONLY
    bdate DATE,
    student_address VARCHAR(150),
    phone VARCHAR(15), -- REGEX
    landline VARCHAR(10), -- Msh mota2aked
    gender BIT,
    email VARCHAR(50), -- REGEX
    PRIMARY KEY (student_id),
    FOREIGN KEY (dpt_id) REFERENCES department(dpt_id)
);

CREATE TABLE unregistered_instructor (
    instructor_id INT,
    Instructor_temp_pw_hash INT,
    PRIMARY KEY (instructor_id)
);

CREATE TABLE instructor (
    instructor_id INT CHECK (instructor_id >= 0),
    Instructor_pw_hash INT,
    dpt_id TINYINT,
    instructor_name VARCHAR(60),
    phone VARCHAR(15), -- REGEX
    email VARCHAR(50), -- REGEX
    office_hrs VARCHAR(120), -- Plain text field
    PRIMARY KEY (instructor_id),
    FOREIGN KEY (dpt_id) REFERENCES department(dpt_id)
);


CREATE TABLE course (
    course_code VARCHAR(7), -- 4 letters for dpt. abbreviation and 3 for course #
    course_name VARCHAR(40),
    course_description VARCHAR(255), -- Plain text field
    offering_dpt TINYINT,
    credit_hrs TINYINT,
    PRIMARY KEY (course_code),
    FOREIGN KEY (offering_dpt) REFERENCES department(dpt_id)
);

CREATE TABLE course_prereq (
    course_code VARCHAR(7),
    preq_id VARCHAR(7),
    PRIMARY KEY (course_code, preq_id),
    FOREIGN KEY (course_code) REFERENCES course(course_code),
    FOREIGN KEY (preq_id) REFERENCES course(course_code)
);

CREATE TABLE semester (
    season TINYINT CHECK (season BETWEEN 0 AND 3), -- 0 for Fall, 1 for Spring, 2 for Summer, 3 for Winter 
    academic_year CHAR(4),
    starting_date DATE,
    end_date DATE,
    PRIMARY KEY (season, academic_year)
);

CREATE TABLE instructs (
    instructor_id INT,
    course_code VARCHAR(7),
    season TINYINT,
    academic_year CHAR(4),
    PRIMARY KEY (instructor_id, course_code, season, academic_year),
    FOREIGN KEY (instructor_id) REFERENCES instructor(instructor_id),
    FOREIGN KEY (course_code) REFERENCES course(course_code),
    FOREIGN KEY (season, academic_year) REFERENCES semester(season, academic_year)
);

CREATE TABLE attends (
    student_id INT,
    season TINYINT,
    academic_year CHAR(4),
    max_credit_hrs INT,
    credit_hrs INT,
    semester_gpa FLOAT CHECK (semester_gpa BETWEEN 0.0 AND 4.0),
    PRIMARY KEY (student_id, season, academic_year),
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (season, academic_year) REFERENCES semester(season, academic_year)
);

CREATE TABLE grades (
    course_code VARCHAR(7),
    student_id INT,
    season TINYINT,
    academic_year CHAR(4),
    midterm_grade TINYINT,
    year_work TINYINT,
    final_grade TINYINT,
    PRIMARY KEY (course_code, student_id, season, academic_year),
    FOREIGN KEY (course_code) REFERENCES course(course_code),
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (season, academic_year) REFERENCES semester(season, academic_year)
);

CREATE TABLE grading_criteria (
    course_code VARCHAR(7),
    season TINYINT,
    academic_year CHAR(4),
    midterm_full TINYINT,
    year_work_full TINYINT,
    final_full TINYINT,
    midterm_weight TINYINT CHECK (midterm_weight BETWEEN 0 AND 100), -- Represented as percentage
    year_work_weight TINYINT CHECK (year_work_weight BETWEEN 0 AND 100), -- Represented as percentage
    final_weight TINYINT CHECK (final_weight BETWEEN 0 AND 100), -- Represented as percentage
    PRIMARY KEY (course_code, season, academic_year),
    FOREIGN KEY (course_code) REFERENCES course(course_code),
    FOREIGN KEY (season, academic_year) REFERENCES semester(season, academic_year)
);

CREATE TABLE course_note (
    course_code VARCHAR(7),
    instructor_id INT,
    season TINYINT,
    academic_year CHAR(4),
    Week_no TINYINT,
    PRIMARY KEY (instructor_id, course_code, season, academic_year, Week_no),
    FOREIGN KEY (instructor_id, course_code, season, academic_year) REFERENCES instructs(instructor_id, course_code, season, academic_year)
);

CREATE TABLE warning_class (
    warning_id TINYINT,
    warning_description VARCHAR(120),
    PRIMARY KEY (warning_id)
);

CREATE TABLE warning (
    student_id INT,
    warning_id TINYINT,
    issuing_admin_id TINYINT,
    PRIMARY KEY (student_id, warning_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (warning_id) REFERENCES warning_class(warning_id),
    FOREIGN KEY (issuing_admin_id) REFERENCES sys_admin(admin_id)
);

CREATE TABLE ticket (
    ticket_id INT CHECK (ticket_id >= 0),
    author_student_id INT,
    ticket_subject VARCHAR(50),
    body VARCHAR(120),
    ticket_date DATE,
    reply VARCHAR(255),
    replier_admin_id TINYINT,
    PRIMARY KEY (ticket_id),
    FOREIGN KEY (author_student_id) REFERENCES student(student_id),
    FOREIGN KEY (replier_admin_id) REFERENCES sys_admin(admin_id)
);

ALTER TABLE department ADD CONSTRAINT foreign_dep_inst foreign key (head_instructor_id) REFERENCES instructor(instructor_id)