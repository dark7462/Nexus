package com.dark.controller;

import com.dark.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Handles WebSocket messaging for real-time chat updates.
 */
@Controller
public class WebSocketChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * @MessageMapping("/chat/send") listens for messages sent to /app/chat/send.
     * Broadcasts the received message to all subscribers
     * subscribed to the specific topic (e.g., /topic/chat/room123).
     */
    @MessageMapping("/chat/send")
    public void handleChatMessage(@Payload ChatMessage chatMessage) {
        // In a real system, you might validate the room ID here.
        // Route dynamically to each room topic based on roomId in payload.
        System.out.println("Received message for Room " + chatMessage.getRoomId() + ": " + chatMessage.getContent());
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
    }
}