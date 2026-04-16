package com.dark.service;

import com.dark.Exceptions.UserException;
import com.dark.model.User;
import com.dark.repository.UserRepository;
import com.dark.request.SignInRequest;
import com.dark.request.SignUpRequest;
import com.dark.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerUserDetailService customerUserDetailService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void signUpShouldCreateUserWhenEmailNotExists() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setGender("Male");
        request.setEmail("john@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("john@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = authService.signUp(request);

        assertNotNull(response.getToken());
        assertEquals("Registed Successfull..!!!", response.getMessage());
    }

    @Test
    void signUpShouldFailWhenEmailAlreadyExists() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("john@example.com");

        when(userRepository.findByEmail("john@example.com")).thenReturn(new User());

        assertThrows(UserException.class, () -> authService.signUp(request));
    }

    @Test
    void signInShouldReturnTokenWhenCredentialsAreValid() {
        SignInRequest request = new SignInRequest();
        request.setEmail("john@example.com");
        request.setPassword("password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "john@example.com", "hashed", List.of());

        when(customerUserDetailService.loadUserByUsername("john@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        AuthResponse response = authService.signIn(request);

        assertNotNull(response.getToken());
        assertEquals("Login Successfull..!!!", response.getMessage());
    }

    @Test
    void signInShouldFailWhenPasswordInvalid() {
        SignInRequest request = new SignInRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrong");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "john@example.com", "hashed", List.of());

        when(customerUserDetailService.loadUserByUsername("john@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.signIn(request));
    }
}
