package com.dark.service.Posts;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.dark.model.Post;
import com.dark.Execptions.PostException;
import com.dark.Execptions.UserException;

public interface PostService {
	Post createPost(@RequestBody Post post, int userID) throws UserException;

	String deletPost(int postId, int userId) throws PostException, UserException;

	List<Post> findAllPostByUserId(int userId) throws UserException;

	Post findPostById(int postId);

	List<Post> findAllPost();

	Post savePost(int postId, int userId) throws PostException, UserException;

	Post likePost(int postId, int userId) throws PostException, UserException;

}
