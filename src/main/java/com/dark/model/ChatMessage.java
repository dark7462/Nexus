package com.dark.model;

// Note: Assuming this model is intended for WebSocket payload, 
// we are creating a dedicated class mirroring the structure requested.

public class ChatMessage {
    private String roomId;
    private String sender;
    private String content;

    public ChatMessage() {}

    public ChatMessage(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
    }

    // Getters and Setters (Crucial for Spring binding)
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}