package com.cego.chatlog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cego.chatlog.controller.DataController;
import com.cego.chatlog.controller.FlagController;
import com.cego.chatlog.controller.MessageController;
import com.cego.chatlog.controller.SearchController;
import com.cego.chatlog.controller.SlackController;

@SpringBootTest
class ChatlogApplicationTests {

	@Autowired
    private DataController dataController;

	@Autowired
	private FlagController flagController;

	@Autowired
	private MessageController messageController;

	@Autowired
	private SearchController searchController;

	@Autowired
	private SlackController slackController;

    @Test
    void contextLoads() throws Exception {
        assertThat(dataController).isNotNull();
		assertThat(flagController).isNotNull();
		assertThat(messageController).isNotNull();
		assertThat(searchController).isNotNull();
		assertThat(slackController).isNotNull();
    }

}
