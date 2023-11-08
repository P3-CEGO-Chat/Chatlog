package com.cego.chatlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cego.chatlog.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    
}
