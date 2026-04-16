package com.dark.mapper;

import com.dark.model.Comment;
import com.dark.model.Post;
import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.response.CommentDto;
import com.dark.response.PostDto;
import com.dark.response.ReelDto;
import com.dark.response.UserDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DtoMapperTests {

    @Test
    void shouldMapUserToUserDto() {
        User user = new User();
        user.setId(7);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("John@Example.Com");
        user.setGender("Male");
        user.setFollowers(new HashSet<>());
        user.setFollowing(new HashSet<>());

        UserDto dto = DtoMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals(7, dto.getId());
        assertEquals("john", dto.getFirstName());
        assertEquals("doe", dto.getLastName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("male", dto.getGender());
    }

    @Test
    void shouldMapPostWithNestedFields() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Alice");
        user.setEmail("alice@example.com");
        user.setGender("female");

        Comment comment = new Comment();
        comment.setCommentId(10);
        comment.setContent("Nice post");
        comment.setUser(user);

        Post post = new Post();
        post.setPostId(99);
        post.setCaption("hello");
        post.setUser(user);
        post.setComments(new ArrayList<>());
        post.getComments().add(comment);
        post.setLikedUsers(new HashSet<>());
        post.getLikedUsers().add(user);

        PostDto dto = DtoMapper.toPostDto(post);

        assertNotNull(dto);
        assertEquals(99, dto.getPostId());
        assertEquals(1, dto.getComments().size());
        assertEquals(1, dto.getLikedUsers().size());
        assertEquals("alice", dto.getUser().getFirstName());
    }

    @Test
    void shouldMapCommentAndReelAndHandleNulls() {
        assertNull(DtoMapper.toCommentDto(null));

        Comment comment = new Comment();
        comment.setCommentId(2);
        comment.setContent("c");
        comment.setLiked(null);

        CommentDto commentDto = DtoMapper.toCommentDto(comment);
        assertNotNull(commentDto);
        assertTrue(commentDto.getLiked().isEmpty());

        Reels reel = new Reels();
        reel.setId(5);
        reel.setTitle("t");
        reel.setVideo("v");

        ReelDto reelDto = DtoMapper.toReelDto(reel);
        assertNotNull(reelDto);
        assertEquals(5, reelDto.getId());
    }
}
