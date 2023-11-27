package com.cego.chatlog.controller;

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

@Controller
@RequestMapping("/flags")
@CrossOrigin(origins = "http://localhost:3000")
public class FlagController {

    @Autowired
    FlagWordsService flagWordsService;

    @PostMapping("/addflag")
    public @ResponseBody String addNewFlag(@RequestBody FlagWords flagWord) {

        if (flagWordsService.existsByWord(flagWord.getWord())) {
            return "Error: Flag word already exists.";
        }
        flagWordsService.save(flagWord);
        return "Added flag word and description successfully";
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
        return flagWordsService.findAll();
    }

    @GetMapping("/removeflag")
    public @ResponseBody String removeFlag(@RequestParam(value = "word") String word) {
        if (!flagWordsService.existsByWord(word)) {
            return "Error: Flag word doesnt exist.";
        } else {
            flagWordsService.deleteByWord(word);
            return "deleted " + word + " successfully";
        }
    }
}
