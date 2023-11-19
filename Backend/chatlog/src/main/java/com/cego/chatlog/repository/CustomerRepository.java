package com.cego.chatlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cego.chatlog.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
    
}
