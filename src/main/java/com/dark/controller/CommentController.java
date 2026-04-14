package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Comment;
import com.dark.request.CreateCommentRequest;
import com.dark.response.ApiResponse;
import com.dark.response.CommentDto;
import com.dark.mapper.DtoMapper;
import com.dark.service.Comments.CommentService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.CommentException;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/commnet/create/{postId}")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest req, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt) throws UserException {
        Comment comment = new Comment();
        comment.setContent(req.getContent());
        Comment createdComment = commentService.createComment(comment, postId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(DtoMapper.toCommentDto(createdComment), HttpStatus.CREATED);
    }

    @PostMapping("/api/like/{commentId}")
    public ResponseEntity<CommentDto> likeComment(@PathVariable Integer commentId,
            @RequestHeader("Authorization") String jwt) throws CommentException, UserException {
        Comment likedComment = commentService.likeComment(commentId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(DtoMapper.toCommentDto(likedComment), HttpStatus.OK);
    }

    @GetMapping("/api/comment/{commentId}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Integer commentId) {
        return new ResponseEntity<>(DtoMapper.toCommentDto(commentService.findCommentById(commentId)), HttpStatus.OK);
    }

    @GetMapping("/api/post/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable Integer postId) throws PostException {
        List<CommentDto> comments = commentService.getAllCommentsByPost(postId).stream().map(DtoMapper::toCommentDto).collect(Collectors.toList());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/api/comment/{commentId}/{postId}")
    public ResponseEntity<ApiResponse> deletComment(@PathVariable Integer commentId, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt)
            throws CommentException, PostException, UserException {
        commentService.deleteComment(commentId, postId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
    }
}
