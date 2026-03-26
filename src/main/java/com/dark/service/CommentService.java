package com.dark.service;

import java.util.List;

import com.dark.model.Comment;

public interface CommentService {
    public Comment createComment(Comment comment, Integer postId, Integer userId);

    public Comment likeComment(Integer commentId, Integer userId);

    public Comment findCommentById(Integer commentId);

    public List<Comment> getAllCommentsByPost(Integer postId);
    
    public Boolean deleteComment(Integer commentId, Integer postId, Integer userId) throws Exception;
}
