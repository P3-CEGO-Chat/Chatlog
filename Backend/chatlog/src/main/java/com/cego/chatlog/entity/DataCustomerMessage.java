package com.cego.chatlog.entity;

import java.util.Date;

public class DataCustomerMessage {
    private String customerId;
    private String username;
    private String message;
    private Date dateTime;

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

    public Date getDateTime(){
        return this.dateTime;
    }

    public void setDateTime(Date dateTime){
        this.dateTime = dateTime;
    }
}
