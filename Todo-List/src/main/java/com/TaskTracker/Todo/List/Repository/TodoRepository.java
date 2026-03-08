package com.TaskTracker.Todo.List.Repository;

import com.TaskTracker.Todo.List.Entity.Todo;
import com.TaskTracker.Todo.List.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {
    public Page<Todo> findByUser(User user, Pageable pageable);
}