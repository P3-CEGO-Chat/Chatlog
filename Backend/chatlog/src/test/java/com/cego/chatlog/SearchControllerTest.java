package com.cego.chatlog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cego.chatlog.controller.SearchController;
import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.repository.MessageImplementationRepository;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.repository.MessageRepositoryCustom;
import com.cego.chatlog.service.CustomerService;
import com.cego.chatlog.service.FlagWordsService;
import com.cego.chatlog.service.websocket.WebSocketSessionManager;
import com.cego.chatlog.util.Flagger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageRepositoryCustom messageRepositoryCustom;

    @Test
    public void testFullTextSearch() throws Exception {
        // Prepare test data
        List<String> keywords = List.of("slotspil", "poker");
        String dateTimeFrom = "2023-11-21T14:31:00.000Z";
        String dateTimeTo = "2023-11-22T14:31:00.000Z";
        String username = "HanneHeld";
        String customerId = "";
        boolean isFlagged = true;

        // Mock the query result
        List<Object[]> expectedResult = List.of(
            new Object[] {1, "SN1", "Hej alle, er der nogen her der har proevet det nye slotspil og poker?", new Date(1700550300000L), null, "HanneHeld", "HanneHeld"},
            new Object[] {2, "SN1", "wuhuuu det her poker er for vildt, slotspillene er dog ret kedelige", new Date(1700550600000L), null, "HanneHeld", "HanneHeld"}
        );

        String expected = "[" + 
            "[1,\"SN1\",\"Hej alle, er der nogen her der har proevet det nye slotspil og poker?\",\"2023-11-21T07:05:00.000+00:00\",null,\"HanneHeld\",\"HanneHeld\"]," + 
            "[2,\"SN1\",\"wuhuuu det her poker er for vildt, slotspillene er dog ret kedelige\",\"2023-11-21T07:10:00.000+00:00\",null,\"HanneHeld\",\"HanneHeld\"]" +
            "]";

        when(messageRepositoryCustom.fullTextSearch(any(), eq(dateTimeFrom), eq(dateTimeTo), eq(username), eq(customerId), eq(isFlagged))).thenReturn(expectedResult);

        this.mockMvc.perform(get("/search/fulltext?keywords=" + keywords + "&dateTimeFrom=" + dateTimeFrom + "&dateTimeTo=" + dateTimeTo + "&username=" + username + "&customerId=" + customerId + "&isFlagged=" + isFlagged))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(expected)));
    }
}
