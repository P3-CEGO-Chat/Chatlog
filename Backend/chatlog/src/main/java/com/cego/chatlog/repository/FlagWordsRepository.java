package com.cego.chatlog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cego.chatlog.entity.FlagWords;
import java.util.List;


@Repository
public interface FlagWordsRepository extends CrudRepository<FlagWords, Integer> {
    // Check if a flag word exists by the word itself
    boolean existsByWord(String word);

    // delete by word
    void deleteByWord(String word);

    boolean existsById(int id);

    List<FlagWords> findByWord(String word);
}
