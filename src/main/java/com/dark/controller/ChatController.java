package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dark.model.Chat;
import com.dark.model.User;
import com.dark.request.CreatChatRequest;
import com.dark.service.Chats.ChatService;
import com.dark.service.Users.UserService;
import com.dark.Execptions.UserException;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestBody CreatChatRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException {
        User sender = userService.findUserByJwt(jwt);
        User receiver = userService.findById(req.getReciverId());
        if (receiver == null || sender == receiver) {
            throw new UserException("Receiver not found");
        }
        return new ResponseEntity<>(chatService.createChat(sender, receiver), HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findByChatId(@PathVariable Integer chatId) {
        return new ResponseEntity<>(chatService.findByChatId(chatId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> findByUserId(@RequestHeader("Authorization") String jwt) throws UserException {
        return new ResponseEntity<>(chatService.findByUserId(userService.findUserByJwt(jwt).getId()), HttpStatus.OK);
    }
}
