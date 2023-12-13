package com.cego.chatlog;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cego.chatlog.controller.MessageController;
import com.cego.chatlog.entity.DataCustomerMessage;
import com.cego.chatlog.entity.FlagWords;
import com.cego.chatlog.entity.Message;
import com.cego.chatlog.repository.MessageRepository;
import com.cego.chatlog.service.CustomerService;
import com.cego.chatlog.service.FlagWordsService;
import com.cego.chatlog.service.websocket.WebSocketSessionManager;
import com.cego.chatlog.util.Flagger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;


@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private WebSocketSessionManager sessionManager;

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
    void shouldgetMessagesByPage() throws Exception {
        
       List<Object[]> mockMessages = List.of(
            new Object[] {1, "SN1", "Hej alle, er der nogen her der har prøvet det nye slotspil?", new Date(1700550300000L), null, "HanneHeld", "HanneHeld"},
            new Object[] {2, "SN2", "Ja, Hanne! Prøvede det i går, det var ret sjovt.", new Date(1700550600000L), null, "MortenMester", "MortenMester"},
            new Object[] {3, "SN3", "Er der en strategi for at vinde i blackjack, eller er det bare held?", new Date(1700551200000L), null, "LineLucky", "LineLucky"},
            new Object[] {4, "SN4", "Line, det handler om at kende grundlæggende strategi og lidt held!", new Date(1700551500000L), null, "JakobJoker", "JakobJoker"},
            new Object[] {5, "SN10", "Har nogen prøvet live poker her? Er det værd at prøve?", new Date(1700552100000L), null, "SandraSpil", "SandraSpil"}
        );

        when(messageRepository.getStartId(1, 5)).thenReturn(1);
        when(messageRepository.getEndId(1, 5)).thenReturn(5);
        when(messageRepository.findMessagesByStartEndId(1, 5)).thenReturn(mockMessages);

        String expected = "[" +
            "[1,\"SN1\",\"Hej alle, er der nogen her der har prøvet det nye slotspil?\",1700550300000,null,\"HanneHeld\",\"HanneHeld\"]," +
            "[2,\"SN2\",\"Ja, Hanne! Prøvede det i går, det var ret sjovt.\",1700550600000,null,\"MortenMester\",\"MortenMester\"]," +
            "[3,\"SN3\",\"Er der en strategi for at vinde i blackjack, eller er det bare held?\",1700551200000,null,\"LineLucky\",\"LineLucky\"]," +
            "[4,\"SN4\",\"Line, det handler om at kende grundlæggende strategi og lidt held!\",1700551500000,null,\"JakobJoker\",\"JakobJoker\"]," +
            "[5,\"SN10\",\"Har nogen prøvet live poker her? Er det værd at prøve?\",1700552100000,null,\"SandraSpil\",\"SandraSpil\"]" +
        "]";
        
        this.mockMvc.perform(get("/messages/1-5"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(expected)));
    }

    @Test 
    void shouldSendMessages() throws Exception {

        List<FlagWords> flagWordsList = new ArrayList<>();

        FlagWords flagWord1 = new FlagWords();
        flagWord1.setId(1);
        flagWord1.setWord("børneopsparing");
        flagWord1.setDiscription("foruroligende ord");
        flagWordsList.add(flagWord1);

        FlagWords flagWord2 = new FlagWords();
        flagWord2.setId(2);
        flagWord2.setWord("ludoman");
        flagWord2.setDiscription("foruroligende ord");
        flagWordsList.add(flagWord2);

        Iterable<FlagWords> mockFlags = flagWordsList;

        when(flagWordsService.findAll()).thenReturn(mockFlags);

        Map<String, Integer> flaggedMap = new HashMap<>();
        for (FlagWords flagWords : mockFlags){
            flaggedMap.put(flagWords.getWord(), flagWords.getId());
        }

        DataCustomerMessage dataCustomerMessage = new DataCustomerMessage();
        dataCustomerMessage.setCustomerId("SN123");
        dataCustomerMessage.setUsername("TestUser");
        dataCustomerMessage.setMessage("500kr profit fra børneopsparing :)");
        dataCustomerMessage.setDateTime(new Date(1700550300000L));

        assertThat(Flagger.flagChecker(flaggedMap, dataCustomerMessage)).isEqualTo(1);

        Mockito.doNothing().when(customerService).createUpdateUser(dataCustomerMessage);

        Message message = new Message();
        message.setCustomerId(dataCustomerMessage.getCustomerId());
        message.setMessageText(dataCustomerMessage.getMessage());
        message.setDateTime(dataCustomerMessage.getDateTime());
        message.setIsFlagged(1);
        message.setOGUsername(dataCustomerMessage.getUsername());

        Mockito.doNothing().when(sessionManager).emitEvent("newMessage", convertObjectToJSON(message));

        when(messageRepository.save(message)).thenReturn(message);



        String content = "{\n" +
                "    \"customerId\": \"SN123\",\n" +
                "    \"username\": \"TestUser\",\n" +
                "    \"message\": \"500kr profit fra børneopsparing :)\",\n" +
                "    \"dateTime\": \"2023-12-08T09:10:29.531Z\"\n" +
                "}";

        this.mockMvc.perform(post("/messages/receive-message")
            .contentType("application/json")
            .content(content))
            .andExpect(status().isOk())
            .andExpect(content().string("Saved and message sent"));
    }

    @Test
    void shouldGetHighestId() throws Exception {
        when(messageRepository.findHighestMessageId()).thenReturn(5);

        this.mockMvc.perform(get("/messages/find-highest-id"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
    }

    @Test
    void shouldGetPageByMessage() throws Exception {
        when(messageRepository.findHighestMessageId()).thenReturn(76);

        List<Object[]> mockMessages = List.of(
            new Object[] {70, "SN2", "Ja, Hanne! Prøvede det i går, det var ret sjovt.", new Date(1700550600000L), null, "MortenMester", "MortenMester"},
            new Object[] {71, "SN3", "Er der en strategi for at vinde i blackjack, eller er det bare held?", new Date(1700551200000L), null, "LineLucky", "LineLucky"},
            new Object[] {72, "SN4", "Line, det handler om at kende grundlæggende strategi og lidt held!", new Date(1700551500000L), null, "JakobJoker", "JakobJoker"},
            new Object[] {76, "SN10", "Har nogen prøvet live poker her? Er det værd at prøve?", new Date(1700552100000L), null, "SandraSpil", "SandraSpil"}
        );

        when(messageRepository.findMessagesByStartEndId(52, 76)).thenReturn(mockMessages);

        String expected = "[" +
            "[70,\"SN2\",\"Ja, Hanne! Prøvede det i går, det var ret sjovt.\",1700550600000,null,\"MortenMester\",\"MortenMester\"]," +
            "[71,\"SN3\",\"Er der en strategi for at vinde i blackjack, eller er det bare held?\",1700551200000,null,\"LineLucky\",\"LineLucky\"]," +
            "[72,\"SN4\",\"Line, det handler om at kende grundlæggende strategi og lidt held!\",1700551500000,null,\"JakobJoker\",\"JakobJoker\"]," +
            "[76,\"SN10\",\"Har nogen prøvet live poker her? Er det værd at prøve?\",1700552100000,null,\"SandraSpil\",\"SandraSpil\"]" +
        "]";

        this.mockMvc.perform(get("/messages/message-id/71"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(expected)));
    }
}
