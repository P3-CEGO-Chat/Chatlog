package com.cego.chatlog.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "http://localhost:3000") // Replace with your Vue.js app's URL
@RestController
public class SlackController {

    private final String slackWebhookUrl = "https://hooks.slack.com/services/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";

    @PostMapping("/api/sendtoslack")
    public ResponseEntity<String> sendMessageToSlack(@RequestBody String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);

        ResponseEntity<String> response = restTemplate.postForEntity(slackWebhookUrl, request, String.class);
        if (response.getStatusCode().isError()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
        return ResponseEntity.ok(response.getBody());
    }
}

