package com.TaskTracker.Todo.List.Service;

import com.TaskTracker.Todo.List.Dto.SortDto;
import com.TaskTracker.Todo.List.Dto.TodoDto;
import com.TaskTracker.Todo.List.Entity.Todo;
import com.TaskTracker.Todo.List.Entity.User;
import com.TaskTracker.Todo.List.Error.ResourceNotFoundException;
import com.TaskTracker.Todo.List.Repository.TodoRepository;
import com.TaskTracker.Todo.List.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public TodoDto create(User user, TodoDto todoDto) {
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo,TodoDto.class);
    }

    public TodoDto update(Long id, TodoDto todoDto, User user) {
        Optional<Todo> todoFromDb = todoRepository.findById(id);
        if(todoFromDb.isEmpty()){
            throw new ResourceNotFoundException("todo not found with id: "+id);
        }else{
            Todo todo = todoFromDb.get();
            if(!todo.getUser().getId().equals(user.getId())){
                throw new AccessDeniedException("Can't update some other user's todo");
            }else{
                todo.setTitle(todoDto.getTitle());
                todo.setDescription(todoDto.getDescription());
                todo = todoRepository.save(todo);
                return modelMapper.map(todo, TodoDto.class);
            }
        }
    }

    public void delete(Long id, User user) {
        Optional<Todo> todoFromDb = todoRepository.findById(id);
        if(todoFromDb.isEmpty()){
            throw new ResourceNotFoundException("todo not found with id: "+id);
        }else{
            Todo todo = todoFromDb.get();
            if(!todo.getUser().getId().equals(user.getId())){
                throw new AccessDeniedException("Can't delete some other user's todo");
            }else{
                todoRepository.delete(todo);
            }
        }
    }

    public Page<TodoDto> get(int pageNo, int limit, String sortParam, Long id, String title, User user) {
        Specification<Todo> spec = Specification.where(Utils.hasIdGreaterThan(id)).and(Utils.hasTitle(title)).and(Utils.hasUser(user));
        List<SortDto> sortDtos = Utils.JsonStringToSortDto(sortParam);
        List<Order> orders = sortDtos.stream()
                .map( sortDto -> new Order(
                        "desc".equalsIgnoreCase(sortDto.getDirection()) ?
                                Sort.Direction.DESC :
                                Sort.Direction.ASC,
                        sortDto.getField())
                ).toList();
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(pageNo, limit, sort);
        Page<Todo> todoList = todoRepository.findAll(spec, pageable);
        return todoList.map(todo -> modelMapper.map(todo, TodoDto.class));
    }
}
