package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.examples-repo
 * https://stackoverflow.com/questions/47803934/spring-boot-mongo-how-to-refer-to-document-in-other-collections-from-a-collect
 */

@Data
@Document(collection = "role")

public class Role {
  @Id

  private String id;

  private String name;

}
