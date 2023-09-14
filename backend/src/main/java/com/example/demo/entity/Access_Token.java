package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.examples-repo
 * https://stackoverflow.com/questions/47803934/spring-boot-mongo-how-to-refer-to-document-in-other-collections-from-a-collect
 * https://stackoverflow.com/questions/43966601/spring-data-mongodb-how-to-assign-expiration-time-programmatically
 * https://docs.spring.io/spring-data/data-mongo/docs/current-SNAPSHOT/api/org/springframework/data/mongodb/core/index/Indexed.html
 * https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
 */

@Data
@Document(collection = "access_token")

public class Access_Token {
  @Id

  private String id;

  private String token;

  @DBRef
  private User user;

  @Indexed(expireAfterSeconds = 20)
  private LocalDateTime date;

}
