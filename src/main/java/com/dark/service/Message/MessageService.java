package com.dark.service.Message;

import java.util.List;

import com.dark.model.Message;
import com.dark.model.User;

public interface MessageService {
	public Message createMessage(User user, Integer chatId, Message message);
	public List<Message> findByChat(Integer chatId);
}
