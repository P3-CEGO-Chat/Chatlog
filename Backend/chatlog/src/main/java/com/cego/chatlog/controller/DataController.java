package com.cego.chatlog.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cego.chatlog.entity.DataUserMessage;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.entity.User;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DataController {
    
    @Autowired
    UserRepository userRepository;   

    @Autowired 
    MessageRepository messageRepository;
   

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
    public @ResponseBody String hello(){
        return "Hello World";
    }


    @GetMapping("/getMessagesWithUsernames")
    public @ResponseBody String getMessagesWithUsernames(@RequestParam(value = "startId") int startId, @RequestParam(value = "endId") int endId) {
        List<Object[]> messages = messageRepository.findMessagesWithUsernames(startId, endId);
        for (Object[] message : messages) {
            System.out.println(Arrays.toString(message));
        }
        String json = convertObjectToJSON(messages);
        return json;
    }

    private String convertObjectToJSON(List<Object[]> messages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            return null;
        } 
    }

    @GetMapping("/getSearch")
    public @ResponseBody String getSearch(@RequestParam(value = "search") String search) {
        List<Object[]> messages = messageRepository.findSearch(search);
        for (Object[] message : messages) {
            System.out.println(Arrays.toString(message));
        }
        String json = convertObjectToJSON(messages);
        return json;
    }
}
