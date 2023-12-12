package com.cego.chatlog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.cego.chatlog.controller.FlagController;

@SpringBootTest
public class FlagControllerTest {
    
    @Autowired
    private FlagController controller;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
