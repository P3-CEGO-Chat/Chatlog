package com.cego.chatlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    @Autowired 
    MessageRepository messageRepository;
  
    @Autowired
    CustomerService customerService;

    @GetMapping("/{pageId}")
    public ResponseEntity<String> getMessagePage(@PathVariable String pageId) {
        try {
            int startId = messageRepository.getStartId(Integer.parseInt(pageId));
            int endId = messageRepository.getEndId(Integer.parseInt(pageId));
    
            List<Object[]> messages = messageRepository.findMessagesByStartEndId(startId, endId);
            /* for (Object[] message : messages) {
                System.out.println(Arrays.toString(message)); //Just a check to see if it receives the wanted data.
            } */
            //Converting it to JSON, for easier use later.
            String json = convertObjectToJSON(messages);
            return ResponseEntity.ok(json);
        } catch (NumberFormatException error) {
            return ResponseEntity.badRequest().build();
        }
    }

    //Generalized function to convert List<Object[]> to JSON.
    private String convertObjectToJSON(List<Object[]> messages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(messages);
        } catch (JsonProcessingException error) {
            
            return null;
        } 
    }
  
    //Api to post the data that we receive into the database.
    @PostMapping("/send-message")
    public @ResponseBody String addNewUserJSON (@RequestBody DataCustomerMessage dataCustomerMessage) {
        //user.setUserId(user.getCustomerId());
        /* User user = new User(); */    

        //Creating a user for the database, because the database stores both a user and a message seperately
        customerService.createUser(dataCustomerMessage);

        /* user.setCustomerId(dataUserMessage.getCustomerId());
        user.setUsername(dataUserMessage.getUsername());
        user.setUserId(dataUserMessage.getCustomerId()); */
        
        //Creating the message for the database.
        Message message = new Message();
        message.setCustomerId(dataCustomerMessage.getCustomerId());
        message.setMessageText(dataCustomerMessage.getMessage());
        message.setDateTime(dataCustomerMessage.getDateTime());
        message.setIsFlagged(false);
        message.setOGUsername(dataCustomerMessage.getUsername());


        /* userRepository.save(user); */
        messageRepository.save(message);
        return "Saved";
    }

    //Gets the page for an message with a specific ID.
    @GetMapping("/message-id/{messageId}")
    public ResponseEntity<String> getMessageById(@PathVariable String messageId) {
        try {
            Integer highestId = messageRepository.findHighestMessageId();
            int startId = -(Integer.parseInt(messageId) % 25) + Integer.parseInt(messageId) + highestId%25;
            int endId = startId + 24;

            if(startId < 24){
                startId = 1;
                endId = highestId%25+25;
            }
            //this.HighestMessageId = this.messages[this.messages.length - 1].id;
            
            

            // 101-125
            // 129-153
            // endid+ 

    
            List<Object[]> messages = messageRepository.findMessagesByStartEndId(startId, endId);

            String json = convertObjectToJSON(messages);
            return ResponseEntity.ok(json);
        } catch (NumberFormatException error) {
            return ResponseEntity.badRequest().build();
        }
    }
}
