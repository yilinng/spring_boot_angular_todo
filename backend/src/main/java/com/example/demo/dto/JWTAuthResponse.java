package com.example.demo.dto;

import javax.servlet.http.Cookie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
  private String accessToken;
  private String refreshToken;

  private Cookie userCookie;

  private String message;

  private String tokenType = "Bearer";
}
