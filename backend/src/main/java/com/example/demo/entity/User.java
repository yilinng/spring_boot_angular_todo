package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
/*
https://docs.spring.io/spring-data/data-mongo/docs/1.7.0.RELEASE/reference/html/#mapping-usage-references
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.examples-repo
 * https://stackoverflow.com/questions/47803934/spring-boot-mongo-how-to-refer-to-document-in-other-collections-from-a-collect
 * https://www.baeldung.com/spring-mongodb-dbref-annotation
 */

@Data
@Document(collection = "user")

public class User {
  @Id

  private String id;

  private String name;

  @Indexed(unique = true)
  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;

  @DBRef
  private List<Role> roles = new ArrayList<>();

  @DBRef
  private List<Todo> todos = new ArrayList<>();
  // @DocumentReference(lazy = true)
}
