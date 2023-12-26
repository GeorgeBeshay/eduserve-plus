-- This is a table which will keep the system state / global variables, we can edit it later.
CREATE TABLE system_state (
	handle INT,
	current_season TINYINT,
	current_academic_year CHAR(4),
	course_registration_allowed BIT,
	course_withdrawal_allowed BIT,
	PRIMARY KEY (handle),
	FOREIGN KEY (current_season, current_academic_year) REFERENCES semester(season, academic_year)
);