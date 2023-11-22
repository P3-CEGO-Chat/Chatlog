package com.cego.chatlog.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cego.chatlog.service.websocket.WebSocketSessionManager;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class DataController {

    @Autowired
    private WebSocketSessionManager sessionManager;

    //Test if API & Website is working
    @GetMapping("/hello")
    public @ResponseBody String hello(){
        try {
            sessionManager.emitEvent("test", "Hello World");
            return "Hello World";
        } catch (IOException error) {
            return "Failed emitting event";
        }
    }
}