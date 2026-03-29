package com.dark.service.Users;

import java.util.List;

import com.dark.model.User;

public interface UserService {
	public User registerUser(User user);

	public User findById(int id);

	public User findByEmail(String email);

	public List<User> findAll();

	public User updateUser(User user, int id) throws Exception;

	public User followUser(int userId1, int userId2) throws Exception;

	public List<User> searchUser(String query);

	public void deletUser(int userId);

	public User unFollowUser(int userid1, int userid2) throws Exception;

	public String getFollowersCount(int userid);

	public String getFollowingCount(int userid);

	public User findUserByJwt(String jwt);
}
