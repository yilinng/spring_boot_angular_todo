package com.example.demo.repository;

import com.example.demo.entity.Todo;

//import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;

public interface TodoRepository extends MongoRepository<Todo, String> {
  @Query("{_id: '?0'}")
  Todo findByTodoId(String id);

}
