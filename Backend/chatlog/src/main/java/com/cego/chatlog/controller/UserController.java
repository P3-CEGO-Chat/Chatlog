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

import com.cego.chatlog.entity.DataUserMessage;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.entity.User;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.repository.UserRepository;

@Controller
public class UserController {
    
    @Autowired
    UserRepository userRepository;   

    @Autowired 
    MessageRepository messageRepository;
    /*@PostMapping(
		value = "/receiveData", consumes = "application/json", produces = "application/json")
	  public User receiveData(@RequestBody User data) {
        userRepository.save(data);
		return data;
    }*/

    

    @PostMapping("/receiveDataJSON")
    public @ResponseBody String addNewUserJSON (@RequestBody DataUserMessage dataUserMessage) {
        //user.setUserId(user.getCustomerId());
        User user = new User();
        user.setCustomerId(dataUserMessage.getCustomerId());
        user.setUsername(dataUserMessage.getUsername());
        user.setUserId(dataUserMessage.getCustomerId());

        Message message = new Message();
        message.setCustomerId(dataUserMessage.getCustomerId());
        message.setMessageText(dataUserMessage.getMessage());
        message.setDateTime(dataUserMessage.getDateTime());


        userRepository.save(user);
        messageRepository.save(message);
        return "Saved";
    }


    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

}
