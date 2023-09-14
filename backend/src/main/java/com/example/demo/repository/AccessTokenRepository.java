package com.example.demo.repository;

import com.example.demo.entity.Access_Token;

import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/*
 * 
 * https://stackoverflow.com/questions/17484153/how-to-delete-items-in-mongorepository-using-query-annotation
 */

@Repository
public interface AccessTokenRepository extends MongoRepository<Access_Token, String> {
  @Query("{token: '?0'}")
  Optional<Access_Token> findByToken(String token);

  @DeleteQuery
  void deleteByToken(String token);

  Boolean existsByToken(String token);

}
