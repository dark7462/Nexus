package com.dark.service.Comments;

import com.dark.Exceptions.CommentException;
import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;
import com.dark.model.Comment;
import com.dark.model.Post;
import com.dark.model.User;
import com.dark.repository.CommentRepository;
import com.dark.repository.PostRepository;
import com.dark.service.Posts.PostService;
import com.dark.service.Users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplimentationTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImplimentation commentService;

    @Test
    void createCommentShouldAttachUserAndPost() {
        Comment comment = new Comment();
        Post post = new Post();
        User user = new User();

        when(userService.findById(1)).thenReturn(user);
        when(postService.findPostById(2)).thenReturn(post);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment created = commentService.createComment(comment, 2, 1);

        assertEquals(user, created.getUser());
        assertEquals(post, created.getPost());
        verify(postRepository).save(post);
    }

    @Test
    void likeCommentShouldFailWhenCommentMissing() {
        when(commentRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(CommentException.class, () -> commentService.likeComment(5, 1));
    }

    @Test
    void deleteCommentShouldSucceedForOwner() throws Exception {
        Post post = new Post();
        User owner = new User();
        owner.setId(1);

        Comment comment = new Comment();
        comment.setCommentId(10);
        comment.setUser(owner);
        post.getComments().add(comment);

        when(postService.findPostById(2)).thenReturn(post);
        when(commentRepository.findById(10)).thenReturn(Optional.of(comment));

        Boolean deleted = commentService.deleteComment(10, 2, 1);

        assertEquals(true, deleted);
        verify(commentRepository).delete(comment);
    }

    @Test
    void getAllCommentsByPostShouldFailWhenPostMissing() {
        when(postService.findPostById(50)).thenReturn(null);
        assertThrows(PostException.class, () -> commentService.getAllCommentsByPost(50));
    }
}
