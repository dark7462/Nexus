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
public class CommentDto {
    private Integer commentId;
    private String content;
    private UserDto user;
    private List<UserDto> liked = new ArrayList<>();
    private LocalDateTime createdAt;
}
