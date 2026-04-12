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
public class ChatDto {
    private Integer id;
    private String chatName;
    private String chatImage;
    private List<UserDto> users = new ArrayList<>();
    private LocalDateTime timestamp;
}
