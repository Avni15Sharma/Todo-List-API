package com.TaskTracker.Todo.List;

import com.TaskTracker.Todo.List.Dto.SortDto;
import com.TaskTracker.Todo.List.Entity.Todo;
import com.TaskTracker.Todo.List.Entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<SortDto> JsonStringToSortDto(String json){
        try{
            if(json == null || json.isEmpty()){
                return new ArrayList<SortDto>();
            }
            return objectMapper.readValue(json, new TypeReference<ArrayList<SortDto>>(){});
        }catch (Exception ex){
            throw new RuntimeException("Invalid Json: "+ex.getMessage());
        }
    }
    public static Specification<Todo> hasUser(User user){
        if(user == null){
            return null;
        }
        Specification<Todo> specification = new Specification<Todo>() {
            @Override
            public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"),user);
            }
        };
        return specification;
    }
    public static Specification<Todo> hasTitle(String title){
        if(title == null || title.isEmpty()){
            return null;
        }
        Specification<Todo> spec = new Specification<Todo>() {
            @Override
            public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("title"), title);
            }
        };
        return spec;
    }

    public static Specification<Todo> hasIdGreaterThan(Long id){
        if(id == null){
            return null;
        }
        Specification<Todo> spec = new Specification<Todo>() {
            @Override
            public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.greaterThan(root.get("id"), id);
                return predicate;
            }
        };
        return spec;
    }
}
