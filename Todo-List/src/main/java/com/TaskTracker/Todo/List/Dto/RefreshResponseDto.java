package com.TaskTracker.Todo.List.Dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
