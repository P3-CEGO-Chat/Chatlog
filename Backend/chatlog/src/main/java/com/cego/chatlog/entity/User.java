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

    public void setUserId(String customerId) {
        convertToUserId(customerId);
    }


    /* 
    *   Converts the customerId to a userId 
    *   The userId is used to identify the user in the database
    *   The userId is a base 5 number, where each digit is a number from 0-4
    *   The userId is converted back to a customerId when the user logs in
    *   '1': Add 1 * 5^3 = 125 to userId
    *   '2': Add 2 * 5^2 = 50 to userId
    *   '3': Add 3 * 5^1 = 15 to userId
    *   '4': Add 4 * 5^0 = 4 to userId
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