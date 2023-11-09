package com.cego.chatlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cego.chatlog.entity.Message;


@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    
}
