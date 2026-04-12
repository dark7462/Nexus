package com.dark.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank(message = "Caption is required")
    private String caption;
    
    private String imageURL;
    private String videoURL;
}
