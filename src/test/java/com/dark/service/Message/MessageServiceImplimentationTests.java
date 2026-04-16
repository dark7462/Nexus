package com.dark.service.Message;

import com.dark.model.Chat;
import com.dark.model.Message;
import com.dark.model.User;
import com.dark.repository.MessageRepository;
import com.dark.service.Chats.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplimentationTests {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private MessageServiceImplimentation messageService;

    @Test
    void createMessageShouldSetUserAndChat() {
        User user = new User();
        Chat chat = new Chat();
        chat.setId(5);
        Message message = new Message();

        when(chatService.findByChatId(5)).thenReturn(chat);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Message saved = messageService.createMessage(user, 5, message);

        assertEquals(user, saved.getUser());
        assertEquals(chat, saved.getChat());
    }

    @Test
    void findByChatShouldDelegateToRepository() {
        when(messageRepository.findByChatId(5)).thenReturn(List.of(new Message()));
        assertEquals(1, messageService.findByChat(5).size());
    }
}
