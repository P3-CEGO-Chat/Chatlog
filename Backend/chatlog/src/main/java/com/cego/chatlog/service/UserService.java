package com.cego.chatlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cego.chatlog.entity.DataUserMessage;
import com.cego.chatlog.entity.User;
import com.cego.chatlog.repository.UserRepository;
import com.cego.chatlog.util.CustomerIdConverter;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public void createUser(DataUserMessage dataUserMessage) {
        User user = new User();
        user.setCustomerId(dataUserMessage.getCustomerId());
        user.setUsername(dataUserMessage.getUsername());
        int userId = CustomerIdConverter.convertToUserId(dataUserMessage.getCustomerId());
        user.setUserId(userId);

        userRepository.save(user);
    }
}
