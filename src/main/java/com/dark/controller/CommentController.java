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
import com.dark.response.ApiResponse;
import com.dark.service.Comments.CommentService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.CommentException;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/commnet/create/{postId}")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt) throws UserException {
        Comment createdComment = commentService.createComment(comment, postId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PostMapping("/api/like/{commentId}")
    public ResponseEntity<Comment> likeComment(@PathVariable Integer commentId,
            @RequestHeader("Authorization") String jwt) throws CommentException, UserException {
        Comment likedComment = commentService.likeComment(commentId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(likedComment, HttpStatus.OK);
    }

    @GetMapping("/api/comment/{commentId}")
    public ResponseEntity<Comment> findCommentById(@PathVariable Integer commentId) {
        return new ResponseEntity<>(commentService.findCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/api/post/{postId}")
    public ResponseEntity<List<Comment>> getAllCommentsByPost(@PathVariable Integer postId) throws PostException {
        return new ResponseEntity<>(commentService.getAllCommentsByPost(postId), HttpStatus.OK);
    }

    @DeleteMapping("/api/comment/{commentId}/{postId}")
    public ResponseEntity<ApiResponse> deletComment(@PathVariable Integer commentId, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt)
            throws CommentException, PostException, UserException {
        commentService.deleteComment(commentId, postId, userService.findUserByJwt(jwt).getId());
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
    }
}
