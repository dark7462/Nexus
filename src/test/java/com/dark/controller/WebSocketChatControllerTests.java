package com.dark.controller;

import com.dark.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WebSocketChatControllerTests {

    @Test
    void shouldSendMessageToRoomTopic() {
        SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        WebSocketChatController controller = new WebSocketChatController(template);

        ChatMessage message = new ChatMessage("room-1", "alice", "hello");
        controller.handleChatMessage(message);

        verify(template).convertAndSend(eq("/topic/chat/room-1"), eq(message));
    }
}
