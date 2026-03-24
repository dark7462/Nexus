package com.dark.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.configration.JwtProvider;
import com.dark.model.User;
import com.dark.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public User registerUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User updateUser(User user, int id) throws Exception {
		User updatedUser = findById(id);
		if(updatedUser == null) {
			return null;
		}
		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setLastName(user.getLastName());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setFollowers(user.getFollowers());
		updatedUser.setFollowing(user.getFollowing());
		updatedUser.setPassword(user.getPassword());
		updatedUser.setGender(user.getGender());
		userRepository.save(updatedUser);
		return updatedUser;
	}

	@Override
	public User followUser(int userId1, int userId2) throws Exception {
		//user1 will unfollow user2
		if(userId1 == userId2) throw new IllegalArgumentException("can't follow yourself");
		
		User user1 = findById(userId1);
		User user2 = findById(userId2);
		
		if(user1 == null || user2 == null) {
			throw new IllegalArgumentException("User not found");
		}
		user1.getFollowing().add(userId2);
		user2.getFollowers().add(userId1);
		
		updateUser(user1, userId1);
		updateUser(user2, userId2);
		
		return user1;
	}
	
	@Override
	public User unFollowUser(int userId1, int userId2) throws Exception {
		//user1 will follow user2
		if(userId1 == userId2) throw new IllegalArgumentException("can't follow yourself");
		
		User user1 = findById(userId1);
		User user2 = findById(userId2);
		
		if(user1 == null || user2 == null) {
			throw new IllegalArgumentException("User not found");
		}
		
		Set<Integer> set = user1.getFollowing();
		if(set == null) {
			set = new HashSet<>();
		}
		if(set.contains(userId2))set.remove(userId2);
		user1.setFollowing(set);
		
		set = user2.getFollowers();
		if(set == null) {
			set = new HashSet<>();
		}
		if(set.contains(userId1))set.remove(userId1);
		user2.setFollowers(set);
		
		updateUser(user1, userId1);
		updateUser(user2, userId2);
		
		return user1;
	}
	
	@Override
	public String getFollowersCount(int userid) {
		User user = findById(userid);
		if(user == null || user.getFollowers() == null) {
			return "0";
		}
		return String.valueOf(user.getFollowers().size());
	}
	
	@Override
	public String getFollowingCount(int userid) {
		User user = findById(userid);
		if(user == null || user.getFollowing() == null) {
			return "0";
		}
		return String.valueOf(user.getFollowing().size());
	}

	@Override
	public List<User> searchUser(String query) {
		return userRepository.searchUser(query);
	}

	@Override
	public void deletUser(int userId) {
		userRepository.deleteById(userId);
		return;
	}

	@Override
	public User findUserByJwt(String jwt) {
		return userRepository.findByEmail(JwtProvider.getEmailFromJwtToken(jwt));
	}
	
}
