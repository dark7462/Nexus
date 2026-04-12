package com.dark.response;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Set<Integer> followers = new HashSet<>();
    private Set<Integer> following = new HashSet<>();
}
