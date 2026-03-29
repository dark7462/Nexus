package com.dark.service.Chats;

import java.util.List;

import com.dark.model.Chat;
import com.dark.model.User;

public interface ChatService {
	public Chat createChat(User sender, User reciver);

	public Chat findByChatId(Integer chatId);

	public List<Chat> findByUserId(Integer UserId);
}
