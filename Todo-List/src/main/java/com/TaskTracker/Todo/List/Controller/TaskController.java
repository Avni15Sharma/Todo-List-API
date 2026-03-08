package com.TaskTracker.Todo.List.Controller;

import com.TaskTracker.Todo.List.Dto.TodoDto;
import com.TaskTracker.Todo.List.Entity.Todo;
import com.TaskTracker.Todo.List.Entity.User;
import com.TaskTracker.Todo.List.Service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TaskController {
    private final TodoService todoService;
    @PostMapping()
    public ResponseEntity<TodoDto> create(@Valid @RequestBody TodoDto todoDto, Authentication authentication){
        return ResponseEntity.ok(todoService.create((User) authentication.getPrincipal(),todoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> update(@Valid @Positive @PathVariable Long id,@Valid @RequestBody TodoDto todoDto, Authentication authentication){
        return ResponseEntity.ok(todoService.update(id,todoDto,(User) authentication.getPrincipal()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication){
        todoService.delete(id, (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    public ResponseEntity<Page<TodoDto>> get(@RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "5") int limit,
                                             @RequestParam(defaultValue = "[{\"field\":\"title\",\"direction\":\"desc\"}]") String sort,
                                             @RequestParam(required = false) Long id,
                                             @RequestParam(required = false) String title,
                                             Authentication authentication){
        return ResponseEntity.ok(todoService.get(pageNo,limit,sort,id,title,(User) authentication.getPrincipal()));
    }

}
