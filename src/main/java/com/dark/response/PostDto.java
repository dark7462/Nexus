package com.dark.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer postId;
    private String caption;
    private String imageURL;
    private String videoURL;
    private UserDto user;
    private List<UserDto> likedUsers = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();
    private LocalDateTime createdAt;
}
