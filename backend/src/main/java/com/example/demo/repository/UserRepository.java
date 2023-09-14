package com.example.demo.repository;

import com.example.demo.entity.User;

import org.springframework.data.mongodb.repository.Query;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/*
https://stackoverflow.com/questions/57762423/existquery-in-spring-data-mongodb
https://docs.spring.io/spring-data/data-mongodb/docs/current/api/org/springframework/data/mongodb/repository/Query.html
*/

//https://stackoverflow.com/questions/16478101/spring-data-mongo-use-or-in-query
public interface UserRepository extends MongoRepository<User, String> {
  @Query("{'$or':[ {email :  ?0}, username :  ?0} ] }")
  User findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  User findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
