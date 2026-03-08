package com.TaskTracker.Todo.List.service;

import com.TaskTracker.Todo.List.Dto.TodoDto;
import com.TaskTracker.Todo.List.Entity.Todo;
import com.TaskTracker.Todo.List.Entity.User;
import com.TaskTracker.Todo.List.Error.ResourceNotFoundException;
import com.TaskTracker.Todo.List.Repository.TodoRepository;
import com.TaskTracker.Todo.List.Service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TodoService todoService;

    @Test
    void create_shouldSaveTodoAndReturnTodoDto_whenValidInputProvided(){
        //arrange
        TodoDto todoDto = new TodoDto();
        todoDto.setTitle("Wake up");
        todoDto.setDescription("Join 5 am club");

        Todo todo = new Todo();
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");

        User user = new User();
        user.setUsername("rkverma");
        user.setPassword("12345678");

        Todo savedTodo = new Todo();
        savedTodo.setId(1L);
        savedTodo.setTitle("Wake up");
        savedTodo.setDescription("Join 5 am club");
        savedTodo.setUser(user);

        TodoDto savedTodoDto = new TodoDto();
        savedTodoDto.setId(1L);
        savedTodoDto.setTitle("Wake up");
        savedTodoDto.setDescription("Join 5 am club");

        when(modelMapper.map(todoDto, Todo.class)).thenReturn(todo);
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);
        when(modelMapper.map(savedTodo,TodoDto.class)).thenReturn(savedTodoDto);

        //act
        TodoDto result = todoService.create(user,todoDto);

        //assert
        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Wake up",result.getTitle());
        assertEquals("Join 5 am club",result.getDescription());
        verify(todoRepository).save(any(Todo.class));
        verify(modelMapper).map(todoDto, Todo.class);
        verify(modelMapper).map(savedTodo,TodoDto.class);
    }

    @Test
    void delete_shouldDeleteTodo_whenValidInputProvided(){
        //arrange
        User user = new User();
        user.setId(10L);
        user.setUsername("rkverma");
        user.setPassword("12345678");

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");
        todo.setUser(user);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        //act
        todoService.delete(1L,user);

        //assert
        verify(todoRepository).delete(todo);
    }

    @Test
    void delete_shouldThrowExc_whenTodoDoesNotExist(){
        //arrange
        User user = new User();
        user.setId(10L);
        user.setUsername("rkverma");
        user.setPassword("12345678");

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        //act and assert
        Exception ex = assertThrows(ResourceNotFoundException.class,()-> todoService.delete(1L,user));
        assertEquals("todo not found with id: "+1L,ex.getMessage());
    }

    @Test
    void delete_shouldThrowExc_whenInputIsInvalid(){
        //arrange
        User todoUser = new User();
        todoUser.setId(10L);

        User otherUser = new User();
        otherUser.setId(11L);

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");
        todo.setUser(todoUser);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        //act and assert
        Exception ex = assertThrows(AccessDeniedException.class,()-> todoService.delete(1L,otherUser));
        assertEquals("Can't delete some other user's todo",ex.getMessage());
    }

    @Test
    void update_shouldThrowExc_whenTodoDoesNotExist(){
        //arrange
        User user = new User();
        user.setId(10L);
        user.setUsername("rkverma");
        user.setPassword("12345678");

        TodoDto todo = new TodoDto();
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        //act and assert
        Exception ex = assertThrows(ResourceNotFoundException.class,()-> todoService.update(1L,todo,user));
        assertEquals("todo not found with id: "+1L,ex.getMessage());
    }

    @Test
    void update_shouldThrowExc_whenInputIsInvalid(){
        //arrange
        User todoUser = new User();
        todoUser.setId(10L);

        User otherUser = new User();
        otherUser.setId(11L);

        TodoDto todo = new TodoDto();
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");

        Todo todoFromDb = new Todo();
        todoFromDb.setId(1L);
        todoFromDb.setTitle("Wake up");
        todoFromDb.setDescription("Join 5 am club");
        todoFromDb.setUser(todoUser);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoFromDb));

        //act and assert
        Exception ex = assertThrows(AccessDeniedException.class,()-> todoService.update(1L,todo,otherUser));
        assertEquals("Can't update some other user's todo",ex.getMessage());
    }

    @Test
    void update_shouldUpdate_whenInputIsValid(){
        //arrange
        User todoUser = new User();
        todoUser.setId(10L);

        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Sleep");
        todo.setDescription("Sleep by 10pm");

        Todo todoFromDb = new Todo();
        todoFromDb.setId(1L);
        todoFromDb.setTitle("Wake up");
        todoFromDb.setDescription("Join 5 am club");
        todoFromDb.setUser(todoUser);

        Todo saved = new Todo();
        saved.setId(1L);
        saved.setTitle("Sleep");
        saved.setDescription("Sleep by 10pm");
        saved.setUser(todoUser);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoFromDb));
        when(todoRepository.save(any(Todo.class))).thenReturn(saved);
        when(modelMapper.map(any(Todo.class), eq(TodoDto.class))).thenReturn(todo);

        //act
        TodoDto updated = todoService.update(1L,todo,todoUser);

        //assert
        assertEquals(1L,updated.getId());
        assertEquals("Sleep",updated.getTitle());
        assertEquals("Sleep by 10pm",updated.getDescription());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void get_shouldReturnTodos(){
        //arrange
        User todoUser = new User();
        todoUser.setId(10L);

        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Wake up");
        todo.setDescription("Join 5 am club");

        Todo todoFromDb = new Todo();
        todoFromDb.setId(1L);
        todoFromDb.setTitle("Wake up");
        todoFromDb.setDescription("Join 5 am club");
        todoFromDb.setUser(todoUser);

        Page<Todo> page = new PageImpl<>(List.of(todoFromDb));
        when(todoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Todo.class),eq(TodoDto.class))).thenReturn(todo);

        //act
        Page<TodoDto> result = todoService.get(0,5,"[]",null,null,todoUser);

        //assert
        assertEquals(1,result.getContent().size());
        assertEquals("Wake up",result.getContent().get(0).getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "'[{\"field\":\"title\",\"direction\":\"asc\"}]', ASC",
            "'[{\"field\":\"title\",\"direction\":\"desc\"}]',DESC"
    })
    void get_shouldApplySorting(String sortParam, String expectedDirection){
        // arrange
        User user = new User();
        user.setId(10L);

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Sleep");
        todo1.setUser(user);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Wake up");
        todo2.setUser(user);

        TodoDto dto1 = new TodoDto();
        dto1.setId(1L);
        dto1.setTitle("Sleep");

        TodoDto dto2 = new TodoDto();
        dto2.setId(2L);
        dto2.setTitle("Wake up");

        Page<Todo> page = new PageImpl<>(List.of(todo1,todo2));
        when(todoRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(todo1, TodoDto.class)).thenReturn(dto1);
        when(modelMapper.map(todo2, TodoDto.class)).thenReturn(dto2);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        //act
        Page<TodoDto> result = todoService.get(0,5,sortParam,null,null,user);

        //assert
        verify(todoRepository).findAll(any(Specification.class),pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        Sort.Order order = pageable.getSort().iterator().next();

        assertEquals(Sort.Direction.valueOf(expectedDirection),order.getDirection());
        assertEquals("title",order.getProperty());

        assertEquals(2,result.getContent().size());
    }
}
