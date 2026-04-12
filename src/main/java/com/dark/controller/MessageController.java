package com.dark.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Message;
import com.dark.service.Message.MessageService;
import com.dark.service.Users.UserService;

@RestController
@RequestMapping("/api/message/")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@PostMapping("create/{chatId}")
	public ResponseEntity<Message> createMessage(@RequestBody Message message,
			@RequestHeader("Authorization") String jwt, @PathVariable Integer chatId) {
		message.setTimeStamp(LocalDateTime.now());
		Message createdMessage = messageService.createMessage(userService.findUserByJwt(jwt), chatId, message);
		return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
	}

	@GetMapping("chat/{chatId}")
	public ResponseEntity<List<Message>> findByChat(@PathVariable Integer chatId) {
		return new ResponseEntity<>(messageService.findByChat(chatId), HttpStatus.OK);
	}
}
