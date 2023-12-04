package com.cego.chatlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cego.chatlog.repository.MessageRepoCustom;
import com.cego.chatlog.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/search")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired 
    MessageRepository messageRepository;
    
    @Autowired
    MessageRepoCustom messageRepoCustom;

    //API to search for a specific string, and returning all messages containing this keyword.
    @GetMapping("/")
    public @ResponseBody String getSearch(@RequestParam(value = "search") String search) {
        List<Object[]> messages = messageRepository.findSearch(search); //SQL Search to retrieve the messages
		//Converting it to JSON, for easier use later.
        String json = convertObjectToJSON(messages);
        return json;
    }

    @GetMapping("/fulltext")
    public @ResponseBody List<Object[]> fullTextSearch(@RequestParam List<String> keywords, @RequestParam String dateTimeFrom, @RequestParam String dateTimeTo, @RequestParam String username, @RequestParam String customerId) {
        return messageRepoCustom.fullTextSearch(keywords, dateTimeFrom, dateTimeTo, username, customerId);
    }

    @GetMapping("/datetime")
    public @ResponseBody List<Object[]> dateTimeSearch(@RequestParam String dateTimeFrom, @RequestParam String dateTimeTo) {
        return messageRepoCustom.dateTime(dateTimeFrom, dateTimeTo);
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
}
