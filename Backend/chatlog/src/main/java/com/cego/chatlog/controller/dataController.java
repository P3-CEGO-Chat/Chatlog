package com.cego.chatlog.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dataController {
    private String customerId;
    private String username;
    private String message;
    private LocalDateTime dateTime;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime.format(formatter);
    }

    public void setDateTime(String date) {
        this.dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    public void getAll() {
        System.out.println(this.customerId);
        System.out.println(this.username);
        System.out.println(this.message);
        System.out.println(this.getDateTime());
    }
}