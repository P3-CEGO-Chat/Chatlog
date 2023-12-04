package com.cego.chatlog.repository;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepositoryCustom {
    List<Object[]> fullTextSearch(List<String> keywords, String dateTimeFrom, String dateTimeTo, String username, String customerId);

    List<Object[]> dateTime(String dateTimeFrom, String dateTimeTo);
}
