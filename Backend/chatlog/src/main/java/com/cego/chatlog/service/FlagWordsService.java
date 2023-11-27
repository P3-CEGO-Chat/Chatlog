package com.cego.chatlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.repository.FlagWordsRepository;

@Service
public class FlagWordsService {

    @Autowired
    private FlagWordsRepository flagWordsRepository;

    @Transactional
    public void deleteByWord(String word) {
        flagWordsRepository.deleteByWord(word);
    }

    public boolean existsByWord(String word) {
        return flagWordsRepository.existsByWord(word);
    }

    public void save(FlagWords flagWord) {
        flagWordsRepository.save(flagWord);
    }

    public Iterable<FlagWords> findAll() {
        return flagWordsRepository.findAll();
    }
}   
