package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.example.demo.payload.AccessTokenDto;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.SignUpDto;

public interface AuthService {
  Map<String, String> login(LoginDto loginDto);

  String register(SignUpDto signupDto);

  String logout(AccessTokenDto accessToken);

  HashMap<String, String> GetRefreshtoken(AccessTokenDto accessToken);

  Cookie generateCookie(String user_id);

  Optional<String> readServletCookie(HttpServletRequest request, String name);

  HashMap<String, String> getUser(String usernameOrEmail);

  HashMap<String, List<String>> findAllEmail();

  // String refreshtoken(HttpServletRequest request);

}
