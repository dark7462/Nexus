package com.dark.service.Chats;

import com.dark.Exceptions.UserException;
import com.dark.model.Chat;
import com.dark.model.User;
import com.dark.repository.ChatRepository;
import com.dark.service.Users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplimentationTests {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatServiceImplimentation chatService;

    @Test
    void createChatShouldReturnExistingChat() {
        User sender = new User();
        User receiver = new User();
        Chat existing = new Chat();

        when(chatRepository.findChatByUsersId(sender, receiver)).thenReturn(existing);

        Chat result = chatService.createChat(sender, receiver);

        assertEquals(existing, result);
    }

    @Test
    void createChatShouldCreateWhenMissing() {
        User sender = new User();
        User receiver = new User();

        when(chatRepository.findChatByUsersId(sender, receiver)).thenReturn(null);
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chat result = chatService.createChat(sender, receiver);

        assertNotNull(result.getTimeStamp());
        assertEquals(2, result.getUsers().size());
    }

    @Test
    void findByChatIdShouldReturnNullWhenMissing() {
        when(chatRepository.findById(4)).thenReturn(Optional.empty());
        assertEquals(null, chatService.findByChatId(4));
    }

    @Test
    void findByUserIdShouldThrowWhenUserMissing() {
        when(userService.findById(5)).thenReturn(null);
        assertThrows(UserException.class, () -> chatService.findByUserId(5));
    }

    @Test
    void findByUserIdShouldReturnChatsWhenUserExists() throws Exception {
        User user = new User();
        when(userService.findById(5)).thenReturn(user);
        when(chatRepository.findByUsersId(5)).thenReturn(List.of(new Chat()));

        assertEquals(1, chatService.findByUserId(5).size());
    }
}
