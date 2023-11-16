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
import com.cego.chatlog.repository.MessageRepoCustom;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.repository.UserRepository;
import com.cego.chatlog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DataController {
    
    @Autowired
    UserRepository userRepository;   

    @Autowired 
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepoCustom messageRepoCustom;

   
    //Api to post the data that we receive into the database.
    @PostMapping("/receiveDataJSON")
    public @ResponseBody String addNewUserJSON (@RequestBody DataUserMessage dataUserMessage) {
        //user.setUserId(user.getCustomerId());
        /* User user = new User(); */    

        //Creating a user for the database, because the database stores both a user and a message seperately
        userService.createUser(dataUserMessage);

        /* user.setCustomerId(dataUserMessage.getCustomerId());
        user.setUsername(dataUserMessage.getUsername());
        user.setUserId(dataUserMessage.getCustomerId()); */
        
        //Creating the message for the database.
        Message message = new Message();
        message.setCustomerId(dataUserMessage.getCustomerId());
        message.setMessageText(dataUserMessage.getMessage());
        message.setDateTime(dataUserMessage.getDateTime());


        /* userRepository.save(user); */
        messageRepository.save(message);
        return "Saved";
    }

    //Test if API & Website is working
    @GetMapping("/hello")
    public @ResponseBody String hello(){
        return "Hello World";
    }
	
	//Generalized function to convert List<Object[]> to JSON.
    private String convertObjectToJSON(List<Object[]> messages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            return null;
        } 
    }
	
	//API to search for a specific string, and returning all messages containing this keyword.
    @GetMapping("/getSearch")
    public @ResponseBody String getSearch(@RequestParam(value = "search") String search) {
        List<Object[]> messages = messageRepository.findSearch(search); //SQL Search to retrieve the messages
        for (Object[] message : messages) {
            System.out.println(Arrays.toString(message)); //Just a check to see if it receives the wanted data.
        }
		//Converting it to JSON, for easier use later.
        String json = convertObjectToJSON(messages);
        return json;
    }

    @GetMapping("/search-fulltext")
    public @ResponseBody String searchfulltext(@RequestParam(value = "keyword") String keyword, @RequestParam(value = "keyword2") String keyword2){
        List<Object[]> messages = messageRepository.findSearchFullText(keyword, keyword2);
        String json = convertObjectToJSON(messages);
        return json;
    }

    @GetMapping("/search-fulltext-custom")
    public @ResponseBody List<Object[]> fullTextSearch(@RequestParam List<String> keywords, @RequestParam String username) {
        return messageRepoCustom.fullTextSearch(keywords, username);
    }
}
