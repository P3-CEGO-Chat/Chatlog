package com.cego.chatlog.service;

import java.util.List;

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

    public List<FlagWords> findByWord(String word) {
        return flagWordsRepository.findByWord(word);
    }

    public FlagWords updateFlag(int id, FlagWords updatedFlag) {
        FlagWords existingFlag = findById(id);

        if (existingFlag != null) {
            if (existingFlag.getWord() != updatedFlag.getWord()) {
                existingFlag.setWord(updatedFlag.getWord());
            }
            if (existingFlag.getDescription() != updatedFlag.getWord()) {
                existingFlag.setDescription(updatedFlag.getDescription());
            }

            flagWordsRepository.save(updatedFlag);
            return existingFlag;
        }

        return null;
    }

    public FlagWords save(FlagWords flagWord) {
        return flagWordsRepository.save(flagWord);
    }

    public Iterable<FlagWords> findAll() {
        return flagWordsRepository.findAll();
    }
}   


