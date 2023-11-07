package com.cego.chatlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cego.chatlog.controller.dataController;

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
	public dataController receiveData(@RequestBody dataController data) {
		data.getAll();
		return data;
	}

}
