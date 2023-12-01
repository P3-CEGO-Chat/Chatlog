package com.cego.chatlog.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.repository.FlagWordsRepository;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.service.CustomerService;
import com.cego.chatlog.service.FlagWordsService;
import com.cego.chatlog.service.websocket.WebSocketSessionManager;
import com.cego.chatlog.util.Flagger;
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

    @Autowired
    private FlagWordsRepository flagWordsRepository;

    @Autowired
    private FlagWordsService flagWordsService;

    @GetMapping("/find-flagged-messages")
    public ResponseEntity<String> getFlaggedMessages() {
        List<Object[]> flaggedMessages = messageRepository.findFlaggedMessages();
        String json = convertObjectToJSON(flaggedMessages);
        System.out.println(json);   
        return ResponseEntity.ok(json);
    }

    @GetMapping("/{pageId}-{highestMessageId}")
    public ResponseEntity<String> getMessagePage(@PathVariable String pageId, @PathVariable String highestMessageId) {
        try {
            int startId = messageRepository.getStartId(Integer.parseInt(pageId), Integer.parseInt(highestMessageId));
            int endId = messageRepository.getEndId(Integer.parseInt(pageId), Integer.parseInt(highestMessageId));

            List<Object[]> messages = messageRepository.findMessagesByStartEndId(startId, endId);

            // Converting it to JSON, for easier use later.
            String json = convertObjectToJSON(messages);
            return ResponseEntity.ok(json);
        } catch (NumberFormatException error) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Generalized function to convert List<Object[]> to JSON.
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

    // Api to post the data that we receive into the database.
    @PostMapping("/send-message")
    public @ResponseBody String addNewUserJSON(@RequestBody DataCustomerMessage dataCustomerMessage) {
        try {
            List<String> flaggedList = new ArrayList<String>();
            for (FlagWords flagWords : flagWordsService.findAll()){
                flaggedList.add(flagWords.getWord());
            }
            boolean isFlagged = Flagger.flagChecker(flaggedList ,dataCustomerMessage);
            //Creating a user for the database, because the database stores both a user and a message seperately
            customerService.createUser(dataCustomerMessage);

            /*
             * user.setCustomerId(dataUserMessage.getCustomerId());
             * user.setUsername(dataUserMessage.getUsername());
             * user.setUserId(dataUserMessage.getCustomerId());
             */

            // Creating the message for the database.
            Message message = new Message();
            message.setCustomerId(dataCustomerMessage.getCustomerId());
            message.setMessageText(dataCustomerMessage.getMessage());
            message.setDateTime(dataCustomerMessage.getDateTime());
            message.setIsFlagged(isFlagged);
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
    public ResponseEntity<String> getHighestId() {
        Integer highestId = messageRepository.findHighestMessageId();
        return ResponseEntity.ok(highestId.toString());
    }
    
    

    // Gets the page for an message with a specific ID.
    @GetMapping("/message-id/{messageId}")
    public ResponseEntity<String> getMessageById(@PathVariable String messageId) {
        try {

            Integer highestId = messageRepository.findHighestMessageId();
            // int endId = -(Integer.parseInt(messageId) % 25) + Integer.parseInt(messageId)
            // + highestId%25;
            // int startId = endId - 24;

            int temppage = (int) Math.ceil(((double) highestId - Integer.parseInt(messageId) + 1) / 25);
            int startId = highestId - (temppage * 25) + 1;
            int endId = startId + 24;

            System.out.println(temppage);

            /*
             * if(startId < 24){
             * startId = 1;
             * endId = highestId%25+25;
             * }
             */

            if(startId < 1){
                startId = 1;
                endId = highestId%25+25;
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
