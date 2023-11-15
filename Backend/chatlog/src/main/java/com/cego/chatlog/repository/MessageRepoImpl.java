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
    public List<Object[]> fullTextSearch(List<String> keywords, String username) {
        
        String baseQuery = "SELECT chatlog.message.message_id, chatlog.message.customer_id, chatlog.message.message_text, chatlog.message.date_time, chatlog.user.username FROM chatlog.message LEFT JOIN chatlog.user ON chatlog.message.customer_id = chatlog.user.customer_id WHERE chatlog.user.username LIKE :username ORDER BY chatlog.message.message_id";

        StringBuilder fullTextSearch = new StringBuilder();
        for (int i = 0; i < keywords.size(); i++) {
            fullTextSearch.append("MATCH(chatlog.message.message_text) AGAINST(:keyword");
            fullTextSearch.append(i);
            fullTextSearch.append(" IN NATURAL LANGUAGE MODE)");
            if (i < keywords.size() - 1) {
                fullTextSearch.append(" AND ");
            }
        }
        System.out.println("----testst----" + ((int)(keywords.size())));
        
        String finalQuery = baseQuery.replace("WHERE", "WHERE " + fullTextSearch.toString() + " AND ");

        System.out.println("Final SQL query: " + finalQuery);
        Query query = entityManager.createNativeQuery(finalQuery);
        for (int i = 0; i < keywords.size(); i++) {
            query.setParameter("keyword" + i, keywords.get(i));
        }     
        query.setParameter("username", username + "%");

        return query.getResultList();
    }
}
