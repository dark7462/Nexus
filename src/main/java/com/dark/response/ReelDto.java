package com.dark.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReelDto {
    private Integer id;
    private String title;
    private String video;
    private UserDto user;
}
