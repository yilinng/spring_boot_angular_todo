package com.example.demo.dto;

import lombok.Data;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
/*
 * 
 * We use UserDto class to transfer the data between the controller layer
 * and the view layer. We also use UserDto class for form binding.
 */

public class UserDto {

  @Id
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
  @NotEmpty(message = "Email should not be empty")
  @Email
  private String email;
  @NotEmpty(message = "Password should not be empty")
  private String password;
}
