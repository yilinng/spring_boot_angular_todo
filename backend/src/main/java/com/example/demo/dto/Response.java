package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
  @JsonProperty("response")
  private ResponseDetailsDto response;

  @JsonProperty("postList")
  private List<Todo> postList;

  @JsonProperty("postDetails")
  private Todo post;

  @JsonProperty("userDetails")
  private User user;

  @JsonProperty("userList")
  private List<User> userList;

}
