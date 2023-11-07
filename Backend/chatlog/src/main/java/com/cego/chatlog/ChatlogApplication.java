package com.cego.chatlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class Data {
	private String customerId;
	private String username;
	private String message;
	private String dateTime;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void getAll() {
		System.out.println(this.customerId);
		System.out.println(this.username);
		System.out.println(this.message);
		System.out.println(this.dateTime);
	}
}

@SpringBootApplication
@RestController
public class ChatlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatlogApplication.class, args);
	}
	// hello command
	@GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
      return String.format("Hello %s!", name);
    }

	@PostMapping(
		value = "/receiveData", consumes = "application/json", produces = "application/json")
	public Data receiveData(@RequestBody Data data) {
		data.getAll();
		return data;
	}

}
