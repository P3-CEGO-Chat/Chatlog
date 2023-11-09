package com.cego.chatlog.entity;

import org.springframework.beans.factory.annotation.Autowired;

import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.repository.UserRepository;

public class DataUserMessage {
    private String customerId;
    private String username;
    private String message;
    private String dateTime;

    public String getCustomerId(){
        return this.customerId;
    }
    
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }   

    public String getDateTime(){
        return this.dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
}
