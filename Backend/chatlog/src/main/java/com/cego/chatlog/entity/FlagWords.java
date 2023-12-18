package com.cego.chatlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FlagWords {
    @Id
    private int id;
    private String word;
    private String description;
    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String flagword) {
        this.word = flagword;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
