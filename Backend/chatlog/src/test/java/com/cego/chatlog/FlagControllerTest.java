package com.cego.chatlog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.cego.chatlog.controller.FlagController;
import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.service.FlagWordsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FlagController.class)
public class FlagControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlagWordsService flagWordsService;

    private String convertObjectToJSON(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Log the error and handle it appropriately
            return null;
        }
    }

    @Test
    void shouldAddFlag() throws Exception {

        // Prepare test data
        FlagWords flagWord = new FlagWords();
        flagWord.setId(1);
        flagWord.setWord("test");
        flagWord.setDescription("unknown reason");


        when(flagWordsService.existsByWord(flagWord.getWord())).thenReturn(false);
        when(flagWordsService.save(flagWord)).thenReturn(flagWord);
        when(flagWordsService.findByWord("test")).thenReturn(List.of(flagWord));

        String expected = "{\"success\":\"Added flag word and description successfully\",\"id\":\"1\"}";

        this.mockMvc.perform(post("/flags/addflag")
            .contentType("application/json")
            .content(convertObjectToJSON(flagWord)))
            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @Test
    void shouldNotAddFlag() throws Exception {

        // Prepare test data
        FlagWords flagWord = new FlagWords();
        flagWord.setId(1);
        flagWord.setWord("test");
        flagWord.setDescription("unknown reason");

        when(flagWordsService.existsByWord(flagWord.getWord())).thenReturn(true);
        
        String expected = "Error: Flag word already exists.";

        this.mockMvc.perform(post("/flags/addflag")
            .contentType("application/json")
            .content(convertObjectToJSON(flagWord)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expected));
    }

    @Test
    void shouldGetFlags() throws Exception {
            
            // Prepare test data
            FlagWords flagWord = new FlagWords();
            FlagWords flagWord2 = new FlagWords();
            flagWord.setId(1);
            flagWord.setWord("test");
            flagWord.setDescription("unknown reason");
            flagWord2.setId(2);
            flagWord2.setWord("test2");
            flagWord2.setDescription("unknown reason2");
    
            when(flagWordsService.findAll()).thenReturn(List.of(flagWord, flagWord2));
    
            String expected = "[{\"id\":1,\"word\":\"test\",\"description\":\"unknown reason\"},{\"id\":2,\"word\":\"test2\",\"description\":\"unknown reason2\"}]";
    
            this.mockMvc.perform(get("/flags/getflags")
                .contentType("application/json")
                .content(convertObjectToJSON(flagWord)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void shouldRemoveFlag() throws Exception {
            
            // Prepare test data
            FlagWords flagWord = new FlagWords();
            flagWord.setId(1);
            flagWord.setWord("test");
            flagWord.setDescription("unknown reason");
    
            when(flagWordsService.existsByWord(flagWord.getWord())).thenReturn(true);

            doNothing().when(flagWordsService).deleteByWord(flagWord.getWord());
    
            String expected = "deleted test successfully";
    
            this.mockMvc.perform(get("/flags/removeflag?word=test")
                .contentType("application/json")
                .content(convertObjectToJSON(flagWord)))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void shouldNotRemoveFlag() throws Exception {
            
            // Prepare test data
            FlagWords flagWord = new FlagWords();
            flagWord.setId(1);
            flagWord.setWord("test");
            flagWord.setDescription("unknown reason");
    
            when(flagWordsService.existsByWord(flagWord.getWord())).thenReturn(false);
    
            String expected = "Error: Flag word doesnt exist.";
    
            this.mockMvc.perform(get("/flags/removeflag?word=test")
                .contentType("application/json")
                .content(convertObjectToJSON(flagWord)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expected));
    }

    @Test
    void shouldUpdateFlag() throws Exception {
        // Prepare test data
        FlagWords flagWord = new FlagWords();
        flagWord.setId(1);
        flagWord.setWord("test");
        flagWord.setDescription("unknown reason");

        when(flagWordsService.existsById(flagWord.getId())).thenReturn(true);
        when(flagWordsService.updateFlag(eq(flagWord.getId()), any(FlagWords.class))).thenReturn(flagWord);

        String expected = "Updated flag word and description successfully";

        this.mockMvc.perform(post("/flags/updateflag")
            .contentType("application/json")
            .content(convertObjectToJSON(flagWord)))
            .andExpect(status().isOk())
            .andExpect(content().string(expected));

    }

    @Test
    void shouldNotUpdateFlag() throws Exception {
        // Prepare test data
        FlagWords flagWord = new FlagWords();
        flagWord.setId(1);
        flagWord.setWord("test");
        flagWord.setDescription("unknown reason");

        when(flagWordsService.existsById(flagWord.getId())).thenReturn(false);

        String expected = "Error: Flag with provided ID does not exist.";

        this.mockMvc.perform(post("/flags/updateflag")
            .contentType("application/json")
            .content(convertObjectToJSON(flagWord)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expected));

    }
}
