package com.dark.service.Posts;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.dark.model.Post;

public interface PostService {
	Post createPost(@RequestBody Post post, int userID) throws Exception;

	String deletPost(int postId, int userId) throws Exception;

	List<Post> findAllPostByUserId(int userId) throws Exception;

	Post findPostById(int postId);

	List<Post> findAllPost();

	Post savePost(int postId, int userId) throws Exception;

	Post likePost(int postId, int userId) throws Exception;

}
