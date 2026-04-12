package com.dark.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatChatRequest {
    @NotNull(message = "Receiver ID is required")
    private Integer reciverId;
}