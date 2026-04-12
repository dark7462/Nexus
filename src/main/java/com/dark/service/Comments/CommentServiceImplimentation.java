package com.dark.service.Comments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dark.model.Comment;
import com.dark.model.Post;
import com.dark.model.User;
import com.dark.repository.CommentRepository;
import com.dark.repository.PostRepository;
import com.dark.service.Posts.PostService;
import com.dark.service.Users.UserService;
import com.dark.Exceptions.CommentException;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;

@Service
public class CommentServiceImplimentation implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Override
    public Comment createComment(Comment comment, Integer postId, Integer userId) {

        User user = userService.findById(userId);
        Post post = postService.findPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        Comment comment1 = commentRepository.save(comment);
        post.getComments().add(comment1);

        postRepository.save(post);

        return comment1;
    }

    @Override
    public Comment likeComment(Integer commentId, Integer userId) throws CommentException, UserException {
        Comment comment = findCommentById(commentId);
        if (comment == null) {
            throw new CommentException("Comment not found");
        }
        User user = userService.findById(userId);
        if (user == null) {
            throw new UserException("User not found");
        }

        if (comment.getLiked().contains(user)) {
            comment.getLiked().remove(user);
        } else {
            comment.getLiked().add(user);
        }

        return commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public List<Comment> getAllCommentsByPost(Integer postId) throws PostException {
        Post post = postService.findPostById(postId);
        if (post == null) {
            throw new PostException("Post not found");
        }
        return post.getComments();
    }

    @Override
    public Boolean deleteComment(Integer commentId, Integer postId, Integer userId) throws CommentException, PostException, UserException {
        Post post = postService.findPostById(postId);
        if (post == null) {
            throw new PostException("post Not Found");
        }
        if (findCommentById(commentId) == null) {
            throw new CommentException("comment Not Found");
        }
        if (findCommentById(commentId).getUser().getId() != userId) {
            throw new UserException("User doesn't own this comment..!!");
        }
        if (post.getComments().contains(findCommentById(commentId))) {
            post.getComments().remove(findCommentById(commentId));
            commentRepository.delete(findCommentById(commentId));
            return true;
        }
        throw new CommentException("Invalid comment to delet");
    }

}
