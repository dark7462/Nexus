package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dark.model.User;
import com.dark.request.CreatChatRequest;
import com.dark.response.ChatDto;
import com.dark.mapper.DtoMapper;
import com.dark.service.Chats.ChatService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.UserException;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ChatDto> createChat(@Valid @RequestBody CreatChatRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException {
        User sender = userService.findUserByJwt(jwt);
        User receiver = userService.findById(req.getReciverId());
        if (receiver == null || sender == receiver) {
            throw new UserException("Receiver not found");
        }
        return new ResponseEntity<>(DtoMapper.toChatDto(chatService.createChat(sender, receiver)), HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> findByChatId(@PathVariable Integer chatId) {
        return new ResponseEntity<>(DtoMapper.toChatDto(chatService.findByChatId(chatId)), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> findByUserId(@RequestHeader("Authorization") String jwt) throws UserException {
        List<ChatDto> chats = chatService.findByUserId(userService.findUserByJwt(jwt).getId())
                .stream().map(DtoMapper::toChatDto).collect(Collectors.toList());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }
}
