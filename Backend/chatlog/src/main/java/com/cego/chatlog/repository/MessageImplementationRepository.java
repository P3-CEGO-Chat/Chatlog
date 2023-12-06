package com.cego.chatlog.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Repository
public class MessageImplementationRepository implements MessageRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> fullTextSearch(List<String> keywords, String dateTimeFrom, String dateTimeTo, String username, String customerId, boolean isFlagged) {
        String baseQuery = "SELECT chatlog.message.*, chatlog.customer.current_username FROM chatlog.message LEFT JOIN chatlog.customer ON chatlog.message.customer_id = chatlog.customer.id";

        StringBuilder queryBuilder = new StringBuilder(baseQuery);

        //Checks if the first condition
        boolean firstCondition = true;

        // Keyword search query
        if (keywords != null && keywords.size() >= 1) {
            
            queryBuilder.append(firstCondition ? " WHERE" : " AND");
            firstCondition = false;
            
            // here we make a open parenthesis 
            queryBuilder.append("(");
            // loops through the keywords and adds them to the query
            for (int i = 0; i < keywords.size(); i++) {
                // it uses the match search on the text against the keyword in natural langugage
                queryBuilder.append("MATCH(chatlog.message.message_text) AGAINST(:keyword" + i);
                queryBuilder.append(" IN NATURAL LANGUAGE MODE)");
                // adds 'and' for each keyword but the last
                if (i < keywords.size() - 1) {
                    queryBuilder.append(" AND ");
                }
            }
            // here we close the parenthesis again
            queryBuilder.append(")");
        }
        // Date time search query
        if ((dateTimeFrom != null && dateTimeTo != null) && (!dateTimeFrom.isEmpty() && !dateTimeTo.isEmpty())) {
            queryBuilder.append(firstCondition ? " WHERE" : " AND");
            firstCondition = false;
            // adds datetime search to the query
            queryBuilder.append(" chatlog.message.date_time BETWEEN :dateTimeFrom AND :dateTimeTo");
        }

        // Username search query
        if (username != null && !username.isEmpty()) {
            
            queryBuilder.append(firstCondition ? " WHERE" : " AND");
            firstCondition = false;
            queryBuilder.append(" chatlog.customer.current_username LIKE :username");
        }
        
        // Customer id search query
        if (customerId != null && !customerId.isEmpty()) {
            queryBuilder.append(firstCondition ? " WHERE" : " AND");
            firstCondition = false;
            queryBuilder.append(" chatlog.message.customer_id = :customerId");
        }

        // Flagged search query
        if (isFlagged) {
            queryBuilder.append(firstCondition ? " WHERE" : " AND");
            firstCondition = false;
            queryBuilder.append(" chatlog.message.is_flagged IS NOT NULL");
        }

        queryBuilder.append(" ORDER BY chatlog.message.id");

        //creates the query
        Query query = entityManager.createNativeQuery(queryBuilder.toString(), Object[].class);
        
        // Set parameters keywords
        if (keywords != null && keywords.size() >= 1) {
            for (int i = 0; i < keywords.size(); i++) {
                query.setParameter("keyword" + i, keywords.get(i));
            }
        }
        // Set parameters datetime
        if ((dateTimeFrom != null && dateTimeTo != null) && (!dateTimeFrom.isEmpty() && !dateTimeTo.isEmpty())) {
            query.setParameter("dateTimeFrom", dateTimeFrom);
            query.setParameter("dateTimeTo", dateTimeTo);
        }

        // set parameters for username
        if (username != null && !username.isEmpty()) {
            query.setParameter("username", username + "%");
        }

        // set parameters for customer id
        if (customerId != null && !customerId.isEmpty()) {
            query.setParameter("customerId", customerId);
        }

        // Execute the query
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = (List<Object[]>) query.getResultList();
        return resultList;
    }


    @Override 
    public List<Object[]> dateTime(String dateTimeFrom, String dateTimeTo) {
        String baseQuery = "SELECT chatlog.message.id, chatlog.message.customer_id, chatlog.message.message_text, chatlog.message.date_time, chatlog.message.is_flagged, chatlog.message.og_username, chatlog.customer.current_username FROM chatlog.message LEFT JOIN chatlog.customer ON chatlog.message.customer_id = chatlog.customer.id WHERE chatlog.message.date_time BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY chatlog.message.id";
        Query query = entityManager.createNativeQuery(baseQuery);
        query.setParameter("dateTimeFrom", dateTimeFrom);
        query.setParameter("dateTimeTo", dateTimeTo);
      
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = (List<Object[]>) query.getResultList();
        return resultList;
    }
    
}
