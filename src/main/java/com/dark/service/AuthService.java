package com.dark.service;

import com.dark.Exceptions.UserException;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;

import com.dark.request.SignUpRequest;

public interface AuthService {
    AuthResponse signUp(SignUpRequest signUpRequest) throws UserException;

    AuthResponse signIn(SignInRequest signInRequest);
}
