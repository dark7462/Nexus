package com.dark.service.Users;

import com.dark.Exceptions.UserException;
import com.dark.configuration.JwtProvider;
import com.dark.model.User;
import com.dark.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void updateUserShouldReturnNullWhenUserMissing() throws Exception {
        when(userRepository.findById(10)).thenReturn(Optional.empty());
        assertNull(userService.updateUser(new User(), 10));
    }

    @Test
    void followUserShouldThrowForSelfFollow() {
        assertThrows(UserException.class, () -> userService.followUser(1, 1));
    }

    @Test
    void followUserShouldAddFollowerAndFollowing() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("A");
        user1.setLastName("B");
        user1.setEmail("a@example.com");
        user1.setGender("male");
        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("C");
        user2.setLastName("D");
        user2.setEmail("c@example.com");
        user2.setGender("female");

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.followUser(1, 2);

        assertNotNull(result);
        assertEquals(true, user1.getFollowing().contains(2));
        assertEquals(true, user2.getFollowers().contains(1));
    }

    @Test
    void findUserByJwtShouldResolveEmailFromToken() {
        User expected = new User();
        expected.setEmail("jwtuser@example.com");
        String token = JwtProvider.generateToken(new UsernamePasswordAuthenticationToken("jwtuser@example.com", "x"));

        when(userRepository.findByEmail("jwtuser@example.com")).thenReturn(expected);

        User actual = userService.findUserByJwt("Bearer " + token);
        assertEquals("jwtuser@example.com", actual.getEmail());
    }
}
