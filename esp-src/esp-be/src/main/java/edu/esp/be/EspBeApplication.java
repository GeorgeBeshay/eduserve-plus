package edu.esp.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class EspBeApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(EspBeApplication.class, args);
		System.out.println("Working fine ..");
	}
	@Override
	public void run(String... args) throws Exception {
		String query = "SELECT * FROM student WHERE student_id = 1";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
		for(Map<String, Object> res : results)
			System.out.println(res);
	}
}
