package com.cego.chatlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {
    
    @Autowired
    UserRepository userRepository;   

    /*@PostMapping(
		value = "/receiveData", consumes = "application/json", produces = "application/json")
	  public User receiveData(@RequestBody User data) {
        userRepository.save(data);
		return data;
    }*/

    @PostMapping("/receiveData")
    public @ResponseBody String addNewUser (@RequestParam String customerId, 
           @RequestParam String username) {
        User n = new User();
        n.setCustomerId(customerId);
        n.setUsername(username);
        n.setUserId(customerId);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping("/user")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

}
