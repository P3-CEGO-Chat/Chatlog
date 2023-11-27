package com.cego.chatlog.controller;

import java.io.IOException;
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
import com.cego.chatlog.service.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    @Autowired 
    private MessageRepository messageRepository;
  
    @Autowired
    private CustomerService customerService;

    @Autowired
    private WebSocketSessionManager sessionManager;

    @GetMapping("/{pageId}-{highestMessageId}")
    public ResponseEntity<String> getMessagePage(@PathVariable String pageId, @PathVariable String highestMessageId) {
        try {
            int startId = messageRepository.getStartId(Integer.parseInt(pageId), Integer.parseInt(highestMessageId))-1;
            int endId = messageRepository.getEndId(Integer.parseInt(pageId), Integer.parseInt(highestMessageId))-1;
    
            List<Object[]> messages = messageRepository.findMessagesByStartEndId(startId, endId);
            
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

    // Overload for a single object
    private String convertObjectToJSON(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Log the error and handle it appropriately
            return null;
        }
    }
  
    //Api to post the data that we receive into the database.
    @PostMapping("/send-message")
    public @ResponseBody String addNewUserJSON (@RequestBody DataCustomerMessage dataCustomerMessage) {
        try {
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

            String json = convertObjectToJSON(message);
            

            sessionManager.emitEvent("newMessage", json);
            messageRepository.save(message);
            return "Saved and message sent";
        } catch (IOException error) {
            return "Error saving and sending message";
        }
    }
    @GetMapping("find-highest-id")
    public ResponseEntity<String> getHighestId(){
        Integer highestId = messageRepository.findHighestMessageId();
        return ResponseEntity.ok(highestId.toString());
    }

    //Gets the page for an message with a specific ID.
    @GetMapping("/message-id/{messageId}")
    public ResponseEntity<String> getMessageById(@PathVariable String messageId) {
        try {

            Integer highestId = messageRepository.findHighestMessageId();
            int startId = -(Integer.parseInt(messageId) % 25) + Integer.parseInt(messageId) + highestId%25;
            int endId = startId + 24;

            //100

            //105 - 129
            if(startId < 24){
                startId = 1;
                endId = highestId%25+25;
            }
            //108
            // start 108
            // end 
            if(startId == highestId){
                startId = highestId - highestId%25;
                endId = highestId;
            
            }
            System.out.println(startId);
            System.out.println(endId);
    
            List<Object[]> messages = messageRepository.findMessagesByStartEndId(startId, endId);

            String json = convertObjectToJSON(messages);
            return ResponseEntity.ok(json);
        } catch (NumberFormatException error) {
            return ResponseEntity.badRequest().build();
        }
    }
}
