package com.cego.chatlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Customer {
    @Id
    private String id;
    private int userId;
    private String currentUsername;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}