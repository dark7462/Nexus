package com.dark.mapper;

import java.util.stream.Collectors;
import java.util.ArrayList;

import com.dark.model.Chat;
import com.dark.model.Comment;
import com.dark.model.Message;
import com.dark.model.Post;
import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.response.ChatDto;
import com.dark.response.CommentDto;
import com.dark.response.MessageDto;
import com.dark.response.PostDto;
import com.dark.response.ReelDto;
import com.dark.response.UserDto;

public class DtoMapper {

    public static UserDto toUserDto(User user) {
        if (user == null)
            return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setFollowers(user.getFollowers());
        dto.setFollowing(user.getFollowing());
        return dto;
    }

    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null)
            return null;
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setUser(toUserDto(comment.getUser()));
        dto.setCreatedAt(comment.getCreatedAt());
        if (comment.getLiked() != null) {
            dto.setLiked(comment.getLiked().stream().map(DtoMapper::toUserDto).collect(Collectors.toList()));
        } else {
            dto.setLiked(new ArrayList<>());
        }
        return dto;
    }

    public static PostDto toPostDto(Post post) {
        if (post == null)
            return null;
        PostDto dto = new PostDto();
        dto.setPostId(post.getPostId());
        dto.setCaption(post.getCaption());
        dto.setImageURL(post.getImageURL());
        dto.setVideoURL(post.getVideoURL());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(toUserDto(post.getUser()));

        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream().map(DtoMapper::toCommentDto).collect(Collectors.toList()));
        } else {
            dto.setComments(new ArrayList<>());
        }

        if (post.getLikedUsers() != null) {
            dto.setLikedUsers(post.getLikedUsers().stream().map(DtoMapper::toUserDto).collect(Collectors.toList()));
        } else {
            dto.setLikedUsers(new ArrayList<>());
        }
        return dto;
    }

    public static ChatDto toChatDto(Chat chat) {
        if (chat == null)
            return null;
        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setChatName(chat.getChatName());
        dto.setChatImage(chat.getChatImage());
        dto.setTimestamp(chat.getTimeStamp());
        if (chat.getUsers() != null) {
            dto.setUsers(chat.getUsers().stream().map(DtoMapper::toUserDto).collect(Collectors.toList()));
        } else {
            dto.setUsers(new ArrayList<>());
        }
        return dto;
    }

    public static MessageDto toMessageDto(Message message) {
        if (message == null)
            return null;
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setImage(message.getImage());
        dto.setTimestamp(message.getTimeStamp());
        dto.setUser(toUserDto(message.getUser()));
        // Note: We don't fully map ChatDto inside MessageDto to avoid infinite
        // recursion if Chat maps Messages
        // But since ChatDto doesn't map messages, mapping ChatDto is safe.
        // However, to keep it lightweight, we can just map the basic ChatDto info if
        // needed.
        dto.setChat(toChatDto(message.getChat()));
        return dto;
    }

    public static ReelDto toReelDto(Reels reel) {
        if (reel == null)
            return null;
        ReelDto dto = new ReelDto();
        dto.setId(reel.getId());
        dto.setTitle(reel.getTitle());
        dto.setVideo(reel.getVideo());
        dto.setUser(toUserDto(reel.getUser()));
        return dto;
    }
}
