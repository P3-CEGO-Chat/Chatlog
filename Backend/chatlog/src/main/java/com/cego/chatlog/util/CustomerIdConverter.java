package com.cego.chatlog.util;

public class CustomerIdConverter {
    public static int convertToUserId(String customerId) {
        int userId = 0;
        String tempId = customerId.substring(2);
        int power = tempId.length() - 1;

        for(int i = 0; i < tempId.length(); i++){ 
            userId += (Character.getNumericValue(tempId.charAt(i)) * Math.pow(5, power));
            power--;
        } 

        return userId;
    }
}
