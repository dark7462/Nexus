package com.dark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dark.model.Comment;
import com.dark.service.CommentService;
import com.dark.service.UserService;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/commnet/create/{postId}")
    public Comment createComment(@RequestBody Comment comment, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt) {
        return commentService.createComment(comment, postId, userService.findUserByJwt(jwt).getId());
    }

    @PostMapping("/api/like/{commentId}")
    public Comment likeComment(@PathVariable Integer commentId, @RequestHeader("Authorization") String jwt) {
        return commentService.likeComment(commentId, userService.findUserByJwt(jwt).getId());
    }

    @GetMapping("/api/comment/{commentId}")
    public Comment findCommentById(@PathVariable Integer commentId) {
        return commentService.findCommentById(commentId);
    }

    @GetMapping("/api/post/{postId}")
    public List<Comment> getAllCommentsByPost(@PathVariable Integer postId) {
        return commentService.getAllCommentsByPost(postId);
    }

    @DeleteMapping("/api/comment/{commentId}/{postId}")
    public Boolean deletComment(@PathVariable Integer commentId, @PathVariable Integer postId,
            @RequestHeader("Authorization") String jwt)
            throws Exception {
        return commentService.deleteComment(commentId, postId, userService.findUserByJwt(jwt).getId());
    }
}
