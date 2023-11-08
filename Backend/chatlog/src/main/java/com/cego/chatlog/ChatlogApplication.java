package com.cego.chatlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import graphql.PublicApi;

@SpringBootApplication
public class ChatlogApplication  {


	/*@Autowired
	private JdbcTemplate jdbcTemplate;*/

	public static void main(String[] args) {
		SpringApplication.run(ChatlogApplication.class, args);
	}

	/*@Override
	public void run(String... args) throws Exception {
		String sql = "INSERT INTO user (customerId, username, userId) VALUES (?, ?, ?)";

		int result = jdbcTemplate.update(sql, "S11", "b11a", 1121323);

		if (result > 0) {
			System.out.println("A new row has been inserted.");
		}
	} */
	

}
