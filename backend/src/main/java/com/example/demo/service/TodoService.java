package com.example.demo.service;

import java.util.List;
import java.util.Map;

//import com.example.demo.dto.Response;
import com.example.demo.entity.Todo;

public interface TodoService {
  Todo addTodo(Todo todo);

  Todo editTodo(String todoId, Todo todo);

  Map<String, String> deleteTodo(String todoId);

  List<Todo> getAllTodos();

  Todo getTodoById(String todoId);

  List<Todo> searchTodo(String title);
}
