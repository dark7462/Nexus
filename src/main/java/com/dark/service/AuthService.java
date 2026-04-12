package com.dark.service;

import com.dark.Exceptions.UserException;
import com.dark.model.User;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;

public interface AuthService {
    AuthResponse signUp(User user) throws UserException;
    AuthResponse signIn(SignInRequest signInRequest);
}
