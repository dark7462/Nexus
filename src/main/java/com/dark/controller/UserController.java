package com.dark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.User;
import com.dark.response.UserDto;
import com.dark.mapper.DtoMapper;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.UserException;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/api/users")
	public ResponseEntity<Page<UserDto>> getAllUsers(
			@PageableDefault(size = 20, sort = "firstName") Pageable pageable) {
		Page<UserDto> users = userService.findAll(pageable).map(DtoMapper::toUserDto);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/api/user/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
		return new ResponseEntity<>(DtoMapper.toUserDto(userService.findById(id)), HttpStatus.OK);
	}

	@PutMapping("/api/user")
	public ResponseEntity<UserDto> updateUser(@RequestBody User user, @RequestHeader("Authorization") String jwt)
			throws UserException {
		User updatedUser = userService.updateUser(user, userService.findUserByJwt(jwt).getId());
		if (updatedUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(DtoMapper.toUserDto(updatedUser), HttpStatus.OK);
		}
	}

	@PutMapping("/api/users/follow/{userid2}")
	public ResponseEntity<UserDto> followUser(@RequestHeader("Authorization") String jwt, @PathVariable int userid2)
			throws UserException {
		return new ResponseEntity<>(DtoMapper.toUserDto(userService.followUser(userService.findUserByJwt(jwt).getId(), userid2)),
				HttpStatus.OK);
	}

	@DeleteMapping("/api/users/unfollow/{userid2}")
	public ResponseEntity<UserDto> unFollowUser(@RequestHeader("Authorization") String jwt, @PathVariable int userid2)
			throws UserException {
		return new ResponseEntity<>(DtoMapper.toUserDto(userService.unFollowUser(userService.findUserByJwt(jwt).getId(), userid2)),
				HttpStatus.OK);
	}

	@GetMapping("/api/user/{userid}/followers/count")
	public ResponseEntity<String> getFollowersCount(@PathVariable int userid) {
		return new ResponseEntity<>(userService.getFollowersCount(userid), HttpStatus.OK);
	}

	@GetMapping("/api/user/{userid}/following/count")
	public ResponseEntity<String> getFollowingCount(@PathVariable int userid) {
		return new ResponseEntity<>(userService.getFollowingCount(userid), HttpStatus.OK);
	}

	@GetMapping("/api/user/search")
	public ResponseEntity<Page<UserDto>> searchUser(@RequestParam String query,
			@PageableDefault(size = 20, sort = "firstName") Pageable pageable) {
		Page<UserDto> users = userService.searchUser(query, pageable).map(DtoMapper::toUserDto);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/api/users/profile")
	public ResponseEntity<UserDto> getUserFromToken(@RequestHeader("Authorization") String jwt) {
		User user = userService.findUserByJwt(jwt);
		return new ResponseEntity<>(DtoMapper.toUserDto(user), HttpStatus.OK);
	}
}
