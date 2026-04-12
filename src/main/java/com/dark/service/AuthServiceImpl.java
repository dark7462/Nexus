package com.dark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dark.Exceptions.UserException;
import com.dark.configuration.JwtProvider;
import com.dark.model.User;
import com.dark.repository.UserRepository;
import com.dark.request.SignInRequest;
import com.dark.response.AuthResponse;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	CustomerUserDetailService customerUserDetailService;

	@Override
	public AuthResponse signUp(User user) throws UserException {
		User isExits = userRepository.findByEmail(user.getEmail());
		if (isExits != null) {
			throw new UserException("Email Already Exists..!!");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userRepository.save(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

		String token = JwtProvider.generateToken(authentication);

		return new AuthResponse(token, "Registed Successfull..!!!");
	}

	@Override
	public AuthResponse signIn(SignInRequest signInRequest) {
		Authentication authentication = authenticate(signInRequest.getEmail(), signInRequest.getPassword());

		String token = JwtProvider.generateToken(authentication);

		return new AuthResponse(token, "Login Successfull..!!!");
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = customerUserDetailService.loadUserByUsername(email);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid user email..!!");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid user password..!!");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
