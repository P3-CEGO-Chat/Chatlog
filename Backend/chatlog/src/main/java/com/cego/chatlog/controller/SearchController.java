package com.cego.chatlog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cego.chatlog.repository.MessageRepositoryCustom;

@Controller
@RequestMapping("/search")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    MessageRepositoryCustom messageRepositoryCustom;

    @GetMapping("/fulltext")
    public @ResponseBody List<Object[]> fullTextSearch(@RequestParam List<String> keywords, @RequestParam String dateTimeFrom, @RequestParam String dateTimeTo, @RequestParam String username, @RequestParam String customerId, @RequestParam boolean isFlagged) {
        System.out.println(keywords + dateTimeFrom + dateTimeTo + username + customerId + isFlagged);
        return messageRepositoryCustom.fullTextSearch(keywords, dateTimeFrom, dateTimeTo, username, customerId, isFlagged);
    }
}
