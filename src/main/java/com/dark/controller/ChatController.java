package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dark.model.Chat;
import com.dark.model.User;
import com.dark.request.CreatChatRequest;
import com.dark.service.Chats.ChatService;
import com.dark.service.Users.UserService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public Chat createChat(@RequestBody CreatChatRequest req, @RequestHeader("Authorization") String jwt) {
        User sender = userService.findUserByJwt(jwt);
        User reciver = userService.findById(req.getReciverId());
        if (reciver == null || sender == reciver) {
            throw new RuntimeException("Reciver not found");
        }
        return chatService.createChat(sender, reciver);
    }

    @GetMapping("/{chatId}")
    public Chat findByChatId(@PathVariable Integer chatId) {
        return chatService.findByChatId(chatId);
    }

    @GetMapping("/user/{userId}")
    public List<Chat> findByUserId(@RequestHeader("Authorization") String jwt) {
        return chatService.findByUserId(userService.findUserByJwt(jwt).getId());
    }
}
