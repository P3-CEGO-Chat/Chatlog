package com.cego.chatlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class DataController {

    //Test if API & Website is working
    @GetMapping("/hello")
    public @ResponseBody String hello(){
        return "Hello World";
    }
}
