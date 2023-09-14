package com.example.demo.controller;

import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/todos")
public class TodoController {

  @Autowired
  TodoService todoService;

  @PostMapping("")
  ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
    Todo res = todoService.addTodo(todo);

    return ResponseEntity.ok().body(res);
  }

  @PatchMapping("/{todoId}")
  ResponseEntity<Todo> editTodo(@PathVariable("todoId") String todoId, @RequestBody Todo todo) {
    Todo res = todoService.editTodo(todoId, todo);

    // JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();

    return ResponseEntity.ok().body(res);
  }

  @DeleteMapping("/{todoId}")
  ResponseEntity<Map<String, String>> deleteTodo(@PathVariable("todoId") String todoId) {
    Map<String, String> res = todoService.deleteTodo(todoId);

    // JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();

    return ResponseEntity.ok().body(res);
  }

  @GetMapping("")
  ResponseEntity<List<Todo>> getAllTodos() {
    List<Todo> res = todoService.getAllTodos();

    return ResponseEntity.ok().body(res);
  }

  @GetMapping("/{todoId}")
  ResponseEntity<Todo> getTodoById(@PathVariable("todoId") String todoId) {
    Todo res = todoService.getTodoById(todoId);

    return ResponseEntity.ok().body(res);
  }

  // https://stackoverflow.com/questions/495426/restful-url-design-how-to-query-using-or-between-parameters
  // https://stackoverflow.com/questions/13715811/requestparam-vs-pathvariable
  @GetMapping("/search/")
  ResponseEntity<List<Todo>> searchTodo(@RequestParam("title") String title) {

    List<Todo> res = todoService.searchTodo(title);

    return ResponseEntity.ok().body(res);
  }

}