package com.cego.chatlog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cego.chatlog.controller.SlackController;

@WebMvcTest(SlackController.class)
public class SlackControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplateMock;

    private final String slackWebhookUrl = "https://hooks.slack.com/services/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";


    @Test
    void shouldSendToSlack() throws Exception {
        
        String message = "{ \"text\": \"This is a test message\" }";

        ResponseEntity<String> responseEntity = ResponseEntity.ok("ok");

        when(restTemplateMock.postForEntity(eq(slackWebhookUrl), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        mockMvc.perform(post("/api/sendtoslack")
                .contentType("application/json")
                .content(message))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
