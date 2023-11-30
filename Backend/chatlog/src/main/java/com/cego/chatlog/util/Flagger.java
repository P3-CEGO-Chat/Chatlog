package com.cego.chatlog.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.service.FlagWordsService;

public class Flagger {

    @Autowired
    private static FlagWordsService flagWordsService;

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
