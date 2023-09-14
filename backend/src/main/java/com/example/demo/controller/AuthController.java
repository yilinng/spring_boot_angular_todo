package com.example.demo.controller;

import com.example.demo.payload.AccessTokenDto;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.SignUpDto;

import com.example.demo.service.AuthService;

import com.example.demo.dto.JWTAuthResponse;

import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

  private AuthService authService;

  @PostMapping("/login")

  public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {

    // System.out.println("get cookie init");

    // System.out.println(request.getCookies());

    // https://medium.com/javarevisited/java-null-handling-with-optionals-b2ded1f48b39
    // check cookie is empty

    // Optional<String> getCookie = authService.readServletCookie(request,
    // "user-id");

    // System.out.println("getCookie init");
    // System.out.println(getCookie);

    // if (getCookie != null) {

    // System.out.println(getCookie);

    // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user is login
    // now.");
    // }
    // System.out.println("getCookie after");

    // if(getCookie.isPresent())
    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();

    Map<String, String> token = authService.login(loginDto);

    System.out.println("authService.login(loginDto);");

    System.out.println(token);

    // https://stackoverflow.com/questions/71366306/httponly-cookie-between-rest-apispring-boot-and-vuejs

    Cookie userCookie = authService.generateCookie(token.get("user_id"));

    jwtAuthResponse.setAccessToken(token.get("accessToken"));
    jwtAuthResponse.setRefreshToken(token.get("refreshToken"));
    jwtAuthResponse.setUserCookie(userCookie);
    jwtAuthResponse.setMessage("login success.");

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userCookie.toString()).body(jwtAuthResponse);
  }

  @PostMapping("/signup")
  public ResponseEntity<JWTAuthResponse> registerUser(@RequestBody SignUpDto signUpDto) {

    String FromResponse = authService.register(signUpDto);

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setMessage(FromResponse);

    return ResponseEntity.ok().body(jwtAuthResponse);
  }

  // pass refresh token
  @PostMapping("/logout")
  public ResponseEntity<JWTAuthResponse> logout(@RequestBody AccessTokenDto accessToken) {

    String FromResponse = authService.logout(accessToken);
    Cookie deleteServletCookie = new Cookie("user-id", null);
    deleteServletCookie.setMaxAge(0);

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setMessage(FromResponse);

    jwtAuthResponse.setUserCookie(deleteServletCookie);

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteServletCookie.toString())
        .body(jwtAuthResponse);
  }

  /*
   * @PostMapping("/refreshtoken")
   * public ResponseEntity<Object> getToken(@RequestBody AccessTokenDto
   * accessToken) {
   * 
   * HashMap FromResponse = authService.getRefreshtoken(accessToken);
   * 
   * JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
   * jwtAuthResponse.setAccessToken(FromResponse.get("accessToken"));
   * // jwtAuthResponse.setRefreshToken(token.get("refreshToken"));
   * 
   * return ResponseEntity.ok(jwtAuthResponse);
   * 
   * }
   */
  // https://www.baeldung.com/get-user-in-spring-security
  @GetMapping("/user")
  public ResponseEntity<?> currentUserNameSimple(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    System.out.println("get user");

    String usernameOrEmail = principal.getName();

    HashMap<String, String> findUser = authService.getUser(usernameOrEmail);

    System.out.println(findUser);
    return ResponseEntity.ok().body(findUser);
  }

  @GetMapping("/email")
  public ResponseEntity<?> findAllEmail() {

    HashMap<String, List<String>> findAllEmail = authService.findAllEmail();

    System.out.println(findAllEmail);
    return ResponseEntity.ok().body(findAllEmail);
  }

  /*
   * 
   * To refresh your access token as well as an ID token, you send a token request
   * with a grant_type of refresh_token .
   * Be sure to include the openid scope when you want to refresh the ID token. If
   * the refresh token is valid, then you get back a new access and the refresh
   * token.
   */

  /*
   * 
   * @GetMapping("/refreshtoken")
   * public ResponseEntity<JWTAuthResponse> refreshtoken(HttpServletRequest
   * request) throws Exception {
   * 
   * String token = authService.refreshtoken(request);
   * JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
   * 
   * jwtAuthResponse.setAccessToken(token);
   * 
   * return ResponseEntity.ok(jwtAuthResponse);
   * }
   */

}