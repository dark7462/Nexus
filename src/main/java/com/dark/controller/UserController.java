package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.dark.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/users")
	public List<User> getAllUsers(){
		return userService.findAll();
	}
	
	@GetMapping("/api/user/{id}")
	public User getUserById(@PathVariable int id) {
		return userService.findById(id);
	}
	
	@PutMapping("/api/user")
	public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader("Authorization") String jwt) throws Exception {
		User updatedUser = userService.updateUser(user, userService.findUserByJwt(jwt).getId());
		if(updatedUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		}
	}
	
	@PutMapping("/api/users/follow/{userid2}")
	public User followUser(@RequestHeader("Authorization") String jwt,@PathVariable int userid2) throws Exception {
		return userService.followUser(userService.findUserByJwt(jwt).getId(), userid2);
	}
	
	@DeleteMapping("/api/users/unfollow/{userid2}")
	public User unFollowUser(@RequestHeader("Authorization") String jwt,@PathVariable int userid2) throws Exception {
		return userService.unFollowUser(userService.findUserByJwt(jwt).getId(), userid2);
	}
	
	@GetMapping("/api/user/{userid}/followers/count")
	public String getFollowersCount(@PathVariable int userid) {
		return userService.getFollowersCount(userid);
	}
	
	@GetMapping("/api/user/{userid}/following/count")
	public String getFollowingCount(@PathVariable int userid) {
		return userService.getFollowingCount(userid);
	}
	
	@GetMapping("/api/user/search")
	public List<User> searchUser(@RequestParam String query){
		return userService.searchUser(query);
	}
	
	@GetMapping("/api/users/profile")
	public User getUserFromToken(@RequestHeader("Authorization") String jwt) {
		User user = userService.findUserByJwt(jwt);
		user.setPassword(null);
		return user;
	}
}
