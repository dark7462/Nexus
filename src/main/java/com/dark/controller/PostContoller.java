package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.dark.response.ApiResponse;
import com.dark.response.PostDto;
import com.dark.request.CreatePostRequest;
import com.dark.mapper.DtoMapper;
import com.dark.service.Posts.PostService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;
import com.dark.model.Post;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class PostContoller {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

	@PostMapping("/api/post")
	public ResponseEntity<PostDto> createPostHandler(@Valid @RequestBody CreatePostRequest req, @RequestHeader("Authorization") String jwt)
			throws UserException {
		Post post = new Post();
		post.setCaption(req.getCaption());
		post.setImageURL(req.getImageURL());
		post.setVideoURL(req.getVideoURL());
		Post createdPost = postService.createPost(post, userService.findUserByJwt(jwt).getId());
		return new ResponseEntity<>(DtoMapper.toPostDto(createdPost), HttpStatus.CREATED);
	}

	@DeleteMapping("/api/post/{postId}")
	public ResponseEntity<ApiResponse> DeletePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws PostException, UserException {
		return new ResponseEntity<>(
				new ApiResponse(postService.deletPost(postId, userService.findUserByJwt(jwt).getId()), true),
				HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/posts")
	public ResponseEntity<List<PostDto>> findAllPostByUserIdHandler(@RequestHeader("Authorization") String jwt)
			throws UserException {
		List<PostDto> posts = postService.findAllPostByUserId(userService.findUserByJwt(jwt).getId()).stream().map(DtoMapper::toPostDto).collect(Collectors.toList());
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/api/allposts")
	public ResponseEntity<Page<PostDto>> findAllPostHandler(
			@PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
		Page<PostDto> posts = postService.findAllPost(pageable).map(DtoMapper::toPostDto);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@PutMapping("/api/post/savepost/{postId}")
	public ResponseEntity<PostDto> savePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws PostException, UserException {
		return new ResponseEntity<>(DtoMapper.toPostDto(postService.savePost(postId, userService.findUserByJwt(jwt).getId())),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/api/post/likepost/{postId}")
	public ResponseEntity<PostDto> likePostHandler(@PathVariable Integer postId,
			@RequestHeader("Authorization") String jwt) throws PostException, UserException {
		return new ResponseEntity<>(DtoMapper.toPostDto(postService.likePost(postId, userService.findUserByJwt(jwt).getId())),
				HttpStatus.ACCEPTED);
	}
}
