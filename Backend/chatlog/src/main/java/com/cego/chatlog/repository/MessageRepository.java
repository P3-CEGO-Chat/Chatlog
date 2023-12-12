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
    @Query(value = "SELECT chatlog.message.*, chatlog.customer.current_username FROM chatlog.message LEFT JOIN chatlog.customer ON chatlog.message.customer_id = chatlog.customer.id WHERE chatlog.message.id BETWEEN :startId AND :endId ORDER BY chatlog.message.id", nativeQuery = true)
    List<Object[]> findMessagesByStartEndId(@Param("startId") int startId, @Param("endId") int endId);
    
    default int getEndId(int pageId, int maxMessageId) {
        return Math.max(1, maxMessageId - (pageId - 1) * 25);

    }
    default int getStartId(int pageId, int maxMessageId) {
        return maxMessageId - (pageId - 1) * 25 - 24;
    }

    //public List<Object[]> findSearch(@Param("search") List<Object[]> search);
    @Query("SELECT max(m.id) FROM Message m")
    Integer findHighestMessageId();

    //SQL Search to find all messages with value 1 in isFlagged
    /* @Query(value = "SELECT * FROM chatlog.message WHERE chatlog.message.is_flagged = 1 ORDER BY chatlog.message.id", nativeQuery = true)
    List<Object[]> findFlaggedMessages(); */

    
}
