package com.example.demo.service;

//import com.example.demo.entity.User;
import com.example.demo.dto.Response;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.SignUpDto;

public interface UserService {

  Response registerUser(SignUpDto signUpDto);

  Response login(LoginDto loginDto);

}
