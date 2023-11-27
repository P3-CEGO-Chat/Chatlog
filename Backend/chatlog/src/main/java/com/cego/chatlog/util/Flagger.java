package com.cego.chatlog.util;

import com.cego.chatlog.entity.DataCustomerMessage;

public class Flagger {
    public static Boolean flagChecker(DataCustomerMessage dataCustomerMessage) {
        String message = dataCustomerMessage.getMessage();
        System.out.println("Flagging message: " + message);

        String[] words = {"fuck", "husleje"};

        for (String word: words) {
            if (message.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
