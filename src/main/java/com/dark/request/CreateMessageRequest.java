package com.dark.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequest {
    @NotBlank(message = "Message content cannot be empty")
    private String content;
    
    private String image;
}
