package com.cego.chatlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.Customer;
import com.cego.chatlog.repository.CustomerRepository;
import com.cego.chatlog.util.CustomerIdConverter;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository userRepository;
    
    //Creating a user and saving it in the Database
    public void createUpdateUser(DataCustomerMessage dataCustomerMessage) {
        Customer customer = new Customer();
        customer.setId(dataCustomerMessage.getCustomerId());
        customer.setCurrentUsername(dataCustomerMessage.getUsername());
        int userId = CustomerIdConverter.convertToUserId(dataCustomerMessage.getCustomerId()); // Converting CustomerId to userId from base 10 to base 5
        customer.setUserId(userId);

        userRepository.save(customer);
    }
}
