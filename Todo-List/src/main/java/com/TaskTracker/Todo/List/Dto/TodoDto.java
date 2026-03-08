package com.TaskTracker.Todo.List.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
}
