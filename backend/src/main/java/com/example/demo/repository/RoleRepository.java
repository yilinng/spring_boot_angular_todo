package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  @Query("{ 'name' :  ?0 }")

  Optional<Role> findByName(String name);
}
