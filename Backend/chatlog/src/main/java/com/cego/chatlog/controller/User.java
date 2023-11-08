package com.cego.chatlog.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class User {
    @Id
    private String customerId;
    private int userId;
    private String username;
    


    /*public User(String customerId, String username, int userId) {
        this.customerId = customerId;
        this.userId = userId;
        this.username = username;
        
    }*/

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

    public void setUserId(String customerId) {
        convertToUserId(customerId);
    }

    









    /* 
    *   Converts the customerId to a userId 
    *   The userId is used to identify the user in the database
    *   The userId is a base 5 number, where each digit is a number from 0-4
    *   The userId is converted back to a customerId when the user logs in
    *  @param customerId 
    *  @return userId
    */
    private void convertToUserId(String customerId){
        String tempId = customerId.substring(2);
        int power = tempId.length() - 1;

        for(int i = 0; i < tempId.length(); i++){ 
            this.userId += (Character.getNumericValue(tempId.charAt(i)) * Math.pow(5, power));
            power--;
        } 
    }


}