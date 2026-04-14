package com.dark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.dark.Exceptions.UserException;
import com.dark.request.SignUpRequest;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;
import com.dark.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws UserException {
		AuthResponse response = authService.signUp(signUpRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
		AuthResponse response = authService.signIn(signInRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
