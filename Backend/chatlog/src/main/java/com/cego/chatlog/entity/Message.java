package com.cego.chatlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Message {
    @Id
    private int id;
    private String dateTime;
    private String messageText;
    private String customerId;
    private String ogUsername;
    private Boolean isFlagged;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
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

    public Boolean getIsFlagged() {
        return this.isFlagged;
    }

    public void setIsFlagged(Boolean isFlagged) {
        this.isFlagged = isFlagged;
    }
}
