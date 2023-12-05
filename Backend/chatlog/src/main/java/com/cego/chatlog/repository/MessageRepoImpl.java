package com.cego.chatlog.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Repository
public class MessageRepoImpl implements MessageRepoCustom {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> fullTextSearch(List<String> keywords, String dateTimeFrom, String dateTimeTo, String username, String customerId, boolean isFlagged) {
        String baseQuery = "SELECT chatlog.message.id, chatlog.message.customer_id, chatlog.message.message_text, chatlog.message.date_time, chatlog.message.is_flagged, chatlog.message.og_username, chatlog.customer.current_username FROM chatlog.message LEFT JOIN chatlog.customer ON chatlog.message.customer_id = chatlog.customer.id WHERE chatlog.customer.current_username LIKE :username ORDER BY chatlog.message.id";
        StringBuilder fullTextSearch = new StringBuilder();
        if (keywords.size() != 0) {

            if (keywords.size() > 1) {
                fullTextSearch.append("(");
            }

            for (int i = 0; i < keywords.size(); i++) {
                fullTextSearch.append("MATCH(chatlog.message.message_text) AGAINST(:keyword");
                fullTextSearch.append(i);
                fullTextSearch.append(" IN NATURAL LANGUAGE MODE)");
                if (i < keywords.size() - 1) {
                    fullTextSearch.append(" AND ");
                }
            }

            if (keywords.size() > 1) {
                fullTextSearch.append(")");
            }

            if (dateTimeFrom != "" && dateTimeTo != "") {
                fullTextSearch.append(" AND chatlog.message.date_time BETWEEN :dateTimeFrom AND :dateTimeTo");
            }

            if (isFlagged == true) {
                fullTextSearch.append(" AND chatlog.message.is_flagged IS NOT NULL");
            }

            String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch.toString() + " AND");

            System.out.println("Final query" + finalQuery);
            
            Query query = entityManager.createNativeQuery(finalQuery, Object[].class);
            for (int i = 0; i < keywords.size(); i++) {
                query.setParameter("keyword" + i, keywords.get(i));
            }   

            if (dateTimeFrom != "" && dateTimeTo != "") {
                query.setParameter("dateTimeFrom", dateTimeFrom);
                query.setParameter("dateTimeTo", dateTimeTo);
            }

            if (customerId != "" && customerId != null) {

                finalQuery = finalQuery.replace("chatlog.customer.current_username LIKE :username", "chatlog.message.customer_id = :customerId");
                query = entityManager.createNativeQuery(finalQuery, Object[].class);
                query.setParameter("customerId", customerId);

                for (int i = 0; i < keywords.size(); i++) {
                    query.setParameter("keyword" + i, keywords.get(i));
                }   

                if (dateTimeFrom != "" && dateTimeTo != "") {
                    query.setParameter("dateTimeFrom", dateTimeFrom);
                    query.setParameter("dateTimeTo", dateTimeTo);
                }

            } else {
                query.setParameter("username", username + "%");
            }
            
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = (List<Object[]>) query.getResultList();
            return resultList;

        } else {

            Query query = null;
            StringBuilder fullTextSearch2 = new StringBuilder();
            query = entityManager.createNativeQuery(baseQuery, Object[].class);
            
            if (dateTimeFrom != "" && dateTimeTo != "") {
                fullTextSearch2.append("chatlog.message.date_time BETWEEN :dateTimeFrom AND :dateTimeTo");

                if (isFlagged == true) {
                    fullTextSearch2.append(" AND chatlog.message.is_flagged IS NOT NULL");
                }

                String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch2.toString() + " AND ");
                query = entityManager.createNativeQuery(finalQuery, Object[].class);
                
                if (customerId != "" && customerId != null) {
                    finalQuery = finalQuery.replace("chatlog.customer.current_username LIKE :username", "chatlog.message.customer_id = :customerId");
                    query = entityManager.createNativeQuery(finalQuery, Object[].class);
                    query.setParameter("customerId", customerId);
                } else {
                    query.setParameter("username", username + "%");
                }
                query.setParameter("dateTimeFrom", dateTimeFrom);
                query.setParameter("dateTimeTo", dateTimeTo);

                System.out.println("Final query" + finalQuery);
                @SuppressWarnings("unchecked")
                List<Object[]> resultList = (List<Object[]>) query.getResultList();
                return resultList;
            }
            

            if (customerId != "" && customerId != null) {

                if (isFlagged == true) {
                    fullTextSearch2.append(" chatlog.message.is_flagged IS NOT NULL");
                    String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch2.toString() + " AND ");
                    finalQuery = finalQuery.replace("chatlog.customer.current_username LIKE :username", "chatlog.message.customer_id = :customerId");
                    query = entityManager.createNativeQuery(finalQuery, Object[].class);
                    query.setParameter("customerId", customerId);
                    
                } else {
                    String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch2.toString() + " AND ");
                    finalQuery = finalQuery.replace("chatlog.customer.current_username LIKE :username", "chatlog.message.customer_id = :customerId");
                    query = entityManager.createNativeQuery(finalQuery, Object[].class);
                    query.setParameter("customerId", customerId);
                }
            } else {
                if (isFlagged == true) {
                    fullTextSearch2.append(" chatlog.message.is_flagged IS NOT NULL");
                    String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch2.toString() + " AND ");
                    query = entityManager.createNativeQuery(finalQuery, Object[].class);
                    query.setParameter("username", username + "%");
                } else {
                    query = entityManager.createNativeQuery(baseQuery, Object[].class);
                    query.setParameter("username", username + "%");
                }
            }
            

            @SuppressWarnings("unchecked")
            List<Object[]> resultList = (List<Object[]>) query.getResultList();
            return resultList;
        }
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
