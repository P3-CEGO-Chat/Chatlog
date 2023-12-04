package com.cego.chatlog.util;

import java.util.List;

import com.cego.chatlog.entity.DataCustomerMessage;

public class Flagger {
    public static Boolean flagChecker(List<String> flaggedList ,DataCustomerMessage dataCustomerMessage) {
        String message = dataCustomerMessage.getMessage();

        for (String word: flaggedList) {
            if (message.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
