package com.cego.chatlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class User {
    @Id
    private String customerId;
    private int userId;
    private String username;

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}