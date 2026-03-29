package com.dark.service.Posts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.model.Post;
import com.dark.model.User;
import com.dark.repository.PostRepository;
import com.dark.service.Users.UserService;

@Service
public class PostServiceImplimentation implements PostService {

	@Autowired
	PostRepository postRepositoy;

	@Autowired
	UserService userService;

	@Override
	public Post createPost(Post post, int userID) throws Exception {
		User user = userService.findById(userID);
		if (user == null) {
			throw new Exception("User Not Found");
		}
		post.setUser(user);

		return postRepositoy.save(post);
	}

	@Override
	public String deletPost(int postId, int userId) throws Exception {
		Post post = findPostById(postId);
		if (post == null) {
			throw new Exception("Post Not Exists...!!");
		}
		if (post.getUser().getId() != userId) {
			throw new Exception("User Does't Own this post...!!");
		}
		postRepositoy.delete(post);
		return "Post deleted...!!";
	}

	@Override
	public List<Post> findAllPostByUserId(int userId) throws Exception {
		User user = userService.findById(userId);
		if (user == null) {
			throw new Exception("User Not Found");
		}
		return postRepositoy.findAllPostByUserId(userId);
	}

	@Override
	public Post findPostById(int postId) {
		return postRepositoy.findById(postId).orElse(null);
	}

	@Override
	public List<Post> findAllPost() {
		return postRepositoy.findAll();
	}

	@Override
	public Post savePost(int postId, int userId) throws Exception {
		Post post = findPostById(postId);
		User user = userService.findById(userId);
		if (user == null) {
			throw new Exception("User Not Found");
		}
		if (post == null) {
			throw new Exception("Post Not Exists...!!");
		}

		if (user.getSavedPosts().contains(post)) {
			user.getSavedPosts().remove(post);
		} else {
			user.getSavedPosts().add(post);
		}
		userService.updateUser(user, userId);
		return post;
	}

	@Override
	public Post likePost(int postId, int userId) throws Exception {
		Post post = findPostById(postId);
		User user = userService.findById(userId);
		if (user == null) {
			throw new Exception("User Not Found");
		}
		if (post == null) {
			throw new Exception("Post Not Exists...!!");
		}

		// if liked the unlike
		if (post.getLikedUsers().contains(user)) {
			post.getLikedUsers().remove(user);
		} else {
			post.getLikedUsers().add(user);
		}
		postRepositoy.save(post);

		return post;
	}

}
