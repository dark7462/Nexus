package com.dark.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Integer id;
    private String content;
    private String image;
    private ChatDto chat;
    private UserDto user;
    private LocalDateTime timestamp;
}
