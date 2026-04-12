package com.dark.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReelRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Video URL is required")
    private String video;
}
