package com.dark.service.Message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.model.Message;
import com.dark.model.User;
import com.dark.repository.MessageRepository;
import com.dark.service.Chats.ChatService;


@Service
public class MessageServiceImplimentation implements MessageService{
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ChatService chatService;

	@Override
	public Message createMessage(User user, Integer chatId, Message message) {
		message.setChat(chatService.findByChatId(chatId));
		message.setUser(user);
		return messageRepository.save(message);
	}

	@Override
	public List<Message> findByChat(Integer chatId) {
		return messageRepository.findByChatId(chatId);
	}

}
