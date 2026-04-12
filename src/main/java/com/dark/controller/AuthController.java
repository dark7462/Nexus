package com.dark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.User;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;
import com.dark.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signUp(@RequestBody User user) throws Exception {
		AuthResponse response = authService.signUp(user);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest signInRequest) {
		AuthResponse response = authService.signIn(signInRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
