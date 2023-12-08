package com.cego.chatlog.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Message {
    @Id
    private int id;
    private String customerId;
    private String messageText;
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Date dateTime;
    private int isFlagged;
    private String ogUsername;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOGUsername() {
        return this.ogUsername;
    }

    public void setOGUsername(String ogUsername) {
        this.ogUsername = ogUsername;
    }

    public int getIsFlagged() {
        return this.isFlagged;
    }

    public void setIsFlagged(int isFlagged) {
        this.isFlagged = isFlagged;
    }
}
