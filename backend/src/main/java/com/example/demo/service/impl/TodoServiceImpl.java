package com.example.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.IAuthenticationFacade;
import com.example.demo.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TodoServiceImpl implements TodoService {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  private IAuthenticationFacade authenticationFacade;

  /**
   * Method to add post
   * 
   * @param todo contents of the todo
   * @return Response
   */
  @Override
  public Todo addTodo(Todo todo) {
    // Response dto = new Response();
    // ResponseDetailsDto res = new ResponseDetailsDto();
    System.out.println("addtodo....");
    User user = getUser();

    Todo newTodo = new Todo();

    newTodo.setTitle(todo.getTitle());
    newTodo.setContent(todo.getContent());
    newTodo.setCreateDate(LocalDateTime.now());
    newTodo.setUserId(user.getId());

    newTodo = todoRepository.save(newTodo);

    if (ObjectUtils.isEmpty(newTodo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo: add failed.");
    }

    List<Todo> todos = user.getTodos();
    todos.add(newTodo);

    user.setTodos(todos);

    user = userRepository.save(user);

    if (ObjectUtils.isEmpty(user)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User: add todo failed.");
    }

    return newTodo;
  }

  /**
   * Method to edit todo
   * 
   * @param todoId id of the todo
   * @param todo   update contents of the todo
   * @return Response
   */
  @Override
  public Todo editTodo(String todoId, Todo todo) {
    // Response dto = new Response();
    // ResponseDetailsDto res = new ResponseDetailsDto();

    Todo todoDetails = todoRepository.findById(todoId).get();

    if (ObjectUtils.isEmpty(todoDetails)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo: " + todoId + "not exist.");
    }

    todoDetails.setTitle(todo.getTitle());
    todoDetails.setContent(todo.getContent());
    todoDetails.setUpdateDate(LocalDateTime.now());

    todoDetails = todoRepository.save(todoDetails);

    return todoDetails;
  }

  /**
   * Method to delete a post
   * 
   * @param todoId id of the post
   * @return Response
   */
  @Override
  public Map<String, String> deleteTodo(String todoId) {
    // Response dto = new Response();
    // ResponseDetailsDto res = new ResponseDetailsDto();
    Todo todoDetails = todoRepository.findById(todoId).get();

    if (ObjectUtils.isEmpty(todoDetails)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo: " + todoId + "not exist.");
    }

    User user = getUser();

    List<Todo> userTodos = user.getTodos();

    System.out.println("init filterTodos");

    System.out.println(todoId);
    /*
     * mongo id != todoId, because have Object
     * https://stackoverflow.com/questions/11637353/comparing-mongoose-id-and-
     * strings
     */
    for (Todo str : userTodos) {
      System.out.println(str.getId());

      System.out.println(str.getId().equals(todoId));
    }

    // https://www.geeksforgeeks.org/arraylist-removeif-method-in-java/
    userTodos.removeIf(t -> t.getId().equals(todoId));

    System.out.println("filterTodos");

    System.out.println(userTodos);

    user.setTodos(userTodos);

    userRepository.save(user);

    todoRepository.deleteById(todoId);

    Map<String, String> todoMap = new HashMap<String, String>();

    todoMap.put("message", "Todo delete success.");

    return todoMap;
  }

  /**
   * Method to get all the posts
   * 
   * @return Response
   */
  @Override
  public List<Todo> getAllTodos() {
    return todoRepository.findAll();
  }

  /**
   * Method to get post by id
   * 
   * @param postId - id of the post
   * @return Response
   */
  @Override
  public Todo getTodoById(String todoId) {
    // Response dto = new Response();
    // ResponseDetailsDto res = new ResponseDetailsDto();
    LOG.info("Getting user with ID: {}.", todoId);
    Todo todoDetails = todoRepository.findByTodoId(todoId);

    if (ObjectUtils.isEmpty(todoDetails)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo: " + todoId + "not exist.");
    }

    System.out.println("todoDetails from get todo id");
    System.out.println(todoDetails);

    return todoDetails;
  }

  @Override

  public List<Todo> searchTodo(String title) {
    System.out.println("searchTodo title");

    // System.out.println(title);
    // System.out.println(content);

    // filter double quotes
    // https://stackoverflow.com/questions/19299788/how-to-replace-double-quotes-in-a-string-with-in-java
    String filterTitle = title.replaceAll("\"", "");
    // String filterContent = content.replaceAll("\"", "");

    System.out.println(filterTitle);

    List<Todo> allTodos = todoRepository.findAll();
    List<Todo> filteredTodo = new ArrayList<Todo>();

    for (Todo i : allTodos) {
      System.out.println(i.getTitle());

      System.out.println("i.getTitle().contains(title)");
      System.out.println(i.getTitle().toLowerCase().contains(filterTitle.toLowerCase()));

      if (i.getTitle().toLowerCase().contains(filterTitle.toLowerCase())) {
        filteredTodo.add(i);
      }
    }

    return filteredTodo;

  }

  private User getUser() {
    Authentication authentication = authenticationFacade.getAuthentication();

    String username = authentication.getName();

    System.out.println("getUser username");

    System.out.println(username);

    User user = userRepository
        .findByUsernameOrEmail(username, username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: "));

    return user;
  }

  /*
   * public Todo findUserById(String todoId) {
   * Todo todoDetails = todoRepository.findById(todoId).get();
   * 
   * if (!ObjectUtils.isEmpty(todoDetails)) {
   * throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo: " + todoId +
   * "not exist.");
   * }
   * 
   * return todoDetails;
   * }
   */
}