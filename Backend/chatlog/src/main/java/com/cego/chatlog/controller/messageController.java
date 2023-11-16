package com.cego.chatlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cego.chatlog.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired 
    MessageRepository messageRepository;

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
}
