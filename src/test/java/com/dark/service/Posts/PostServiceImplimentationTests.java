package com.dark.service.Posts;

import com.dark.Exceptions.PostException;
import com.dark.Exceptions.UserException;
import com.dark.model.Post;
import com.dark.model.User;
import com.dark.repository.PostRepository;
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
class PostServiceImplimentationTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImplimentation postService;

    @Test
    void createPostShouldFailWhenUserMissing() {
        Post post = new Post();
        when(userService.findById(1)).thenReturn(null);
        assertThrows(UserException.class, () -> postService.createPost(post, 1));
    }

    @Test
    void createPostShouldAssignUserAndSave() throws Exception {
        Post post = new Post();
        User user = new User();
        user.setId(1);

        when(userService.findById(1)).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post saved = postService.createPost(post, 1);

        assertEquals(1, saved.getUser().getId());
    }

    @Test
    void deletePostShouldFailWhenNotOwner() {
        Post post = new Post();
        User owner = new User();
        owner.setId(99);
        post.setUser(owner);

        when(postRepository.findById(5)).thenReturn(Optional.of(post));

        assertThrows(UserException.class, () -> postService.deletPost(5, 1));
    }

    @Test
    void likePostShouldToggleLikeState() throws Exception {
        Post post = new Post();
        User user = new User();
        user.setId(1);
        post.getLikedUsers().add(user);

        when(postRepository.findById(7)).thenReturn(Optional.of(post));
        when(userService.findById(1)).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post result = postService.likePost(7, 1);

        assertEquals(0, result.getLikedUsers().size());
        verify(postRepository).save(post);
    }
}
