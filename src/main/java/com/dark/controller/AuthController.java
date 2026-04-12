package com.dark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dark.configuration.JwtProvider;
import com.dark.model.User;
import com.dark.repository.UserRepository;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;
import com.dark.service.CostumerUserDetailService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CostumerUserDetailService costumerUserDetailService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signUp(@RequestBody User user) throws Exception {

		User isExits = userRepository.findByEmail(user.getEmail());
		if (isExits != null) {
			throw new Exception("Email Already Exists..!!");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userRepository.save(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

		String token = JwtProvider.generateToken(authentication);

		return new ResponseEntity<>(new AuthResponse(token, "Registed Successfull..!!!"), HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest signInRequest) {
		Authentication authentication = authenticate(signInRequest.getEmail(), signInRequest.getPassword());

		String token = JwtProvider.generateToken(authentication);

		return new ResponseEntity<>(new AuthResponse(token, "Login Successfull..!!!"), HttpStatus.OK);
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = costumerUserDetailService.loadUserByUsername(email);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid user email..!!");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid user password..!!");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
