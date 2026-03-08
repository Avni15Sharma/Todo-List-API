package com.TaskTracker.Todo.List.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserDto {
    Long Id;
    String username;
}
