package com.cego.chatlog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cego.chatlog.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    //SQL Search to find all messages and combining them with the corresponding Username.
    @Query(value = "SELECT * FROM chatlog.message WHERE chatlog.message.id BETWEEN :startId AND :endId ORDER BY chatlog.message.id", nativeQuery = true)
    List<Object[]> findMessagesByStartEndId(@Param("startId") int startId, @Param("endId") int endId);

    @Query(value = "SELECT max(id) FROM chatlog.message", nativeQuery = true)
    Integer findMaxMessageId();

    default int getEndId(int pageId) {
        Integer maxMessageId = findMaxMessageId();
        return Math.max(1, maxMessageId - (pageId - 1) * 50);

    }
    default int getStartId(int pageId) {
        Integer maxMessageId = findMaxMessageId();
        return maxMessageId - (pageId - 1) * 50 - 49;
    }

    //public List<Object[]> findSearch(@Param("search") List<Object[]> search);
    
    //SQL Search to find all messages containing a specific keyword.
    @Query(value = "SELECT * FROM chatlog.message WHERE chatlog.message.message_text LIKE %:keyword% ORDER BY chatlog.message.id", nativeQuery = true)
    List<Object[]> findSearch(@Param("keyword") String keyword);


    //SQL fulltext search to find all messages containing a specific keyword. and find containg a specific username
    @Query(value="SELECT chatlog.message.message_id, chatlog.message.customer_id, chatlog.message.message_text, chatlog.message.date_time, chatlog.user.username FROM chatlog.message LEFT JOIN chatlog.user ON chatlog.message.customer_id = chatlog.user.customer_id WHERE MATCH(chatlog.message.message_text) AGAINST(:keyword% IN NATURAL LANGUAGE MODE) AND chatlog.user.username LIKE :keyword2% ORDER BY chatlog.message.message_id", nativeQuery = true)
    List<Object[]> findSearchFullText(@Param("keyword") String keyword, @Param("keyword2") String keyword2);

    
}
