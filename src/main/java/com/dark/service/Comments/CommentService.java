package com.dark.service.Comments;

import java.util.List;

import com.dark.model.Comment;
import com.dark.Exceptions.CommentException;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;

public interface CommentService {
    public Comment createComment(Comment comment, Integer postId, Integer userId);

    public Comment likeComment(Integer commentId, Integer userId) throws CommentException, UserException;

    public Comment findCommentById(Integer commentId);

    public List<Comment> getAllCommentsByPost(Integer postId) throws PostException;

    public Boolean deleteComment(Integer commentId, Integer postId, Integer userId) throws CommentException, PostException, UserException;
}
