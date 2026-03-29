package com.dark.service.Chats;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.model.Chat;
import com.dark.model.User;
import com.dark.repository.ChatRepository;
import com.dark.service.Users.UserService;

@Service
public class ChatServiceImplimentation implements ChatService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserService userService;

    @Override
    public Chat createChat(User sender, User reciver) {
        Chat chat = chatRepository.findChatByUsersId(sender, reciver);
        if (chat != null) {
            return chat;
        }
        chat = new Chat();
        chat.getUsers().add(sender);
        chat.getUsers().add(reciver);
        chat.setTimeStamp(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    @Override
    public Chat findByChatId(Integer chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public List<Chat> findByUserId(Integer UserId) {
        if (userService.findById(UserId) == null) {
            throw new RuntimeException("User not found");
        }
        return chatRepository.findByUsersId(UserId);
    }

}