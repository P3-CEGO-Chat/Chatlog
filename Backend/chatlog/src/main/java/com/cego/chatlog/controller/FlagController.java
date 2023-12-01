package com.cego.chatlog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.service.FlagWordsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/flags")
@CrossOrigin(origins = "http://localhost:3000")
public class FlagController {

    @Autowired
    FlagWordsService flagWordsService;

    @PostMapping("/addflag")
    public @ResponseBody ResponseEntity<String> addNewFlag(@RequestBody FlagWords flagWord) {

        if (flagWordsService.existsByWord(flagWord.getWord())) {
            return new ResponseEntity<>("Error: Flag word already exists.", HttpStatus.BAD_REQUEST);
        }
        flagWordsService.save(flagWord);

        List<FlagWords> findFlagWords = flagWordsService.findByWord(flagWord.getWord());

        Number id = findFlagWords.get(0).getId();

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("success", "Added flag word and description successfully");
        jsonData.put("id", id.toString());

        // Convert the Map to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error: Failed to process JSON data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(json);
    }

    // Exception handler for SQL duplicate entry
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        // You can check the exception details to return a more specific message
        return new ResponseEntity<>("Error: The flag word already exists.", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/getflags")
    public @ResponseBody Iterable<FlagWords> getAllFlags() {
        // This returns a JSON with the users
        System.out.println("Getting all flags" + flagWordsService.findAll());
        for (FlagWords flag : flagWordsService.findAll()) {
            System.out.println("Kig her" + flag.getWord());
        }

        return flagWordsService.findAll();
    }

    @GetMapping("/removeflag")
    public @ResponseBody ResponseEntity<String> removeFlag(@RequestParam(value = "word") String word) {
        if (!flagWordsService.existsByWord(word)) {
            return new ResponseEntity<>("Error: Flag word doesnt exist.", HttpStatus.BAD_REQUEST);
        } else {
            flagWordsService.deleteByWord(word);
            return ResponseEntity.ok("deleted " + word + " successfully");
        }
    }
    @PostMapping("/updateflag")
    public @ResponseBody ResponseEntity<String> updateFlag(@RequestBody FlagWords updatedFlag) {
        int id = updatedFlag.getId();
        if (id <= 0 || !flagWordsService.existsById(id)) { // Assuming IDs are positive
            return new ResponseEntity<>("Error: Flag with provided ID does not exist.", HttpStatus.BAD_REQUEST);
        }
        FlagWords updated = flagWordsService.updateFlag(updatedFlag.getId(), updatedFlag);
        if (updated != null) {
            return ResponseEntity.ok("Updated flag word and description successfully");
        } else {
            return new ResponseEntity<>("Error: Unable to update the flag.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
    
}
