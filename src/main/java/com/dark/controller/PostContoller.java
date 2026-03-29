package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Post;
import com.dark.response.ApiResponse;
import com.dark.service.Posts.PostService;
import com.dark.service.Users.UserService;

@RestController
public class PostContoller {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

	@PostMapping("/api/post")
	public ResponseEntity<Post> createPostHandler(@RequestBody Post post, @RequestHeader("Authorization") String jwt)
			throws Exception {
		return new ResponseEntity<>(postService.createPost(post, userService.findUserByJwt(jwt).getId()),
				HttpStatus.CREATED);
	}

	@DeleteMapping("/api/post/{postId}")
	public ResponseEntity<ApiResponse> DeletePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		return new ResponseEntity<>(
				new ApiResponse(postService.deletPost(postId, userService.findUserByJwt(jwt).getId()), true),
				HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/posts")
	public ResponseEntity<List<Post>> findAllPostByUserIdHandler(@RequestHeader("Authorization") String jwt)
			throws Exception {
		return new ResponseEntity<>(postService.findAllPostByUserId(userService.findUserByJwt(jwt).getId()),
				HttpStatus.OK);
	}

	@GetMapping("/api/allposts")
	public ResponseEntity<List<Post>> findAllPostHandler() throws Exception {
		return new ResponseEntity<>(postService.findAllPost(), HttpStatus.OK);
	}

	@PutMapping("/api/post/savepost/{postId}")
	public ResponseEntity<Post> savePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		return new ResponseEntity<>(postService.savePost(postId, userService.findUserByJwt(jwt).getId()),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/api/post/likepost/{postId}")
	public ResponseEntity<Post> likePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		return new ResponseEntity<>(postService.likePost(postId, userService.findUserByJwt(jwt).getId()),
				HttpStatus.ACCEPTED);
	}
}
