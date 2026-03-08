package com.TaskTracker.Todo.List.Error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private String error;
    private HttpStatus statusCode;
    private LocalDateTime timestamp;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }
}
