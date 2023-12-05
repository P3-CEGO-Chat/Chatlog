package com.cego.chatlog.util;

import java.util.Map;

import com.cego.chatlog.entity.DataCustomerMessage;

public class Flagger {
    public static Integer flagChecker(Map<String, Integer> flaggedMap ,DataCustomerMessage dataCustomerMessage) {
        String message = dataCustomerMessage.getMessage();

        for (String word: flaggedMap.keySet()) {
            if (message.contains(word)) {

                return flaggedMap.get(word);
            }
        }

        return null;
    }
}
