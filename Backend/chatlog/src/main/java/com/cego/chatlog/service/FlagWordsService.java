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

    public boolean existsById(int id) {
        return flagWordsRepository.existsById(id);
    }
    public FlagWords findById(int id) {
        return flagWordsRepository.findById(id).orElse(null);
    }

    public FlagWords updateFlag(int id, FlagWords updatedFlag) {
        FlagWords existingFlag = findById(id);
        if (existingFlag != null) {
            existingFlag.setWord(updatedFlag.getWord());
            existingFlag.setDiscription(updatedFlag.getDescription());

            flagWordsRepository.save(updatedFlag);
            return existingFlag;
        }

        return null;
    }

    public void save(FlagWords flagWord) {
        flagWordsRepository.save(flagWord);
    }

    public Iterable<FlagWords> findAll() {
        return flagWordsRepository.findAll();
    }
}   
