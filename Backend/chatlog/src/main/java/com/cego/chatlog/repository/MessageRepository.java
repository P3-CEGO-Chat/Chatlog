package com.cego.chatlog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cego.chatlog.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByMessageIdBetween(int startId, int endId);
    @Query(value = "SELECT chatlog.message.message_id, chatlog.message.customer_id, chatlog.message.message_text, chatlog.message.date_time, chatlog.user.username, chatlog.user.user_id FROM chatlog.message LEFT JOIN chatlog.user ON chatlog.message.customer_id = chatlog.user.customer_id WHERE chatlog.message.message_id BETWEEN :startId AND :endId ORDER BY chatlog.message.message_id", nativeQuery = true)
    List<Object[]> findMessagesWithUsernames(@Param("startId") int startId, @Param("endId") int endId);
}
