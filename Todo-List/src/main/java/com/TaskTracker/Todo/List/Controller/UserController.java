package com.TaskTracker.Todo.List.Controller;

import com.TaskTracker.Todo.List.Dto.UserDto;
import com.TaskTracker.Todo.List.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    @GetMapping()
    public ResponseEntity<List<UserDto>> GetAllUsers(){
        List<UserDto> list = new ArrayList<>();
        list.add(new UserDto(1L,"Bing"));
        list.add(new UserDto(2L,"Geller"));
        return ResponseEntity.ok(list);
    }
}
