package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//https://spring.io/blog/2021/11/29/spring-data-mongodb-relation-modelling
@Data
@Document(collection = "todo")
public class Todo {
  @Id

  private String id;

  private String title;

  private String content;

  private LocalDateTime createDate;

  private LocalDateTime updateDate;

  private String userId;
}