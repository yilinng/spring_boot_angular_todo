package com.example.demo.service.impl;

import com.example.demo.entity.Access_Token;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.AccessTokenDto;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.SignUpDto;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AccessTokenRepository;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {

  private AuthenticationManager authenticationManager;
  private UserRepository userRepository;
  private AccessTokenRepository accessTokenRepository;
  private PasswordEncoder passwordEncoder;
  private JwtTokenProvider jwtTokenProvider;
  private RoleRepository roleRepository;

  public AuthServiceImpl(
      JwtTokenProvider jwtTokenProvider,
      UserRepository userRepository,
      AccessTokenRepository accessTokenRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.accessTokenRepository = accessTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.roleRepository = roleRepository;
  }

  @Override
  public Map<String, String> login(LoginDto loginDto) {

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginDto.getUsernameOrEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Map<String, String> token = jwtTokenProvider.generateToken(authentication);

    Access_Token new_access_Token = new Access_Token();

    new_access_Token.setToken(token.get("refreshToken"));

    User user = userRepository
        .findByUsernameOrEmail(loginDto.getUsernameOrEmail(),
            loginDto.getUsernameOrEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: "));

    new_access_Token.setUser(user);

    new_access_Token.setDate(LocalDateTime.now());
    // accessTokenRepository.save(new_access_Token);

    token.put("user_id", user.getId());

    return token;
  }

  @Override
  public String register(SignUpDto signUpDto) {
    // add check for username exists in database
    // https://stackoverflow.com/questions/16232833/how-to-respond-with-an-http-400-error-in-a-spring-mvc-responsebody-method-retur
    if (userRepository.existsByUsername(signUpDto.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
    }

    // add check for email exists in database
    if (userRepository.existsByEmail(signUpDto.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
    }

    User user = new User();
    user.setName(signUpDto.getName());
    user.setUsername(signUpDto.getUsername());
    user.setEmail(signUpDto.getEmail());
    user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
    // user.setTodos(null);

    List<Role> roles = new ArrayList<>();
    Role userRole = roleRepository.findByName("ROLE_USER").get();
    System.out.println("find userRole");

    System.out.println(userRole);
    roles.add(userRole);
    user.setRoles(roles);

    userRepository.save(user);

    return "User registered successfully!.";
  }

  // https://www.baeldung.com/get-user-in-spring-security
  @Override
  public String logout(AccessTokenDto accessToken) {
    /*
     * 
     * if (!accessTokenRepository.existsByToken(accessToken.getToken())) {
     * throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
     * "token not found..");
     * }
     * 
     * accessTokenRepository.deleteByToken(accessToken.getToken());
     */
    // https://www.mongodb.com/docs/manual/tutorial/expire-data/
    return "User logout successfully!.";
  }

  // when token expired, have to use refresh token to generate new access token

  @Override
  public HashMap<String, String> GetRefreshtoken(AccessTokenDto accessToken) {
    if (!accessTokenRepository.existsByToken(accessToken.getToken())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "refresh token not found.., please try again.");
    }
    HashMap<String, String> token = new HashMap<String, String>();

    token.put("accessToken", "token");

    return token;
  }

  @Override
  public Cookie generateCookie(String user_id) {
    // System.out.println("generateCookie token " + user_id);
    /*
     * generate cookie by user id
     * https://stackoverflow.com/questions/64607023/how-to-get-current-user-id-in-
     * spring
     * https://reflectoring.io/spring-boot-cookies/
     * https://www.baeldung.com/java-base64-encode-and-decode
     */
    // get username from token
    // String username = jwtTokenProvider.getUsername(token);

    // System.out.println("generateCookie username " + username);

    // User user = userRepository.findByUsername(username).get();

    // System.out.println(" user.getId() " + user.getId());

    String originalId = user_id;
    String encodedId = new String(Base64.encodeBase64(originalId.getBytes()));
    String decodedId = new String(Base64.decodeBase64(encodedId.getBytes()));

    // System.out.println("user id " + originalId + "encodedId " + encodedId +
    // "decodeId " + decodedId);

    Cookie jwtTokenCookie = new Cookie("user-id", encodedId);

    jwtTokenCookie.setMaxAge(86400);
    jwtTokenCookie.setSecure(true);
    jwtTokenCookie.setHttpOnly(true);
    jwtTokenCookie.setPath("/");
    jwtTokenCookie.setDomain("localhost");

    System.out.print("init jwtTokenCookie");

    System.out.print(jwtTokenCookie);

    return jwtTokenCookie;
  }

  @Override
  public Optional<String> readServletCookie(HttpServletRequest request, String name) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> name.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findAny();
  }

  @Override
  public HashMap<String, String> getUser(String usernameOrEmail) {
    User user = userRepository
        .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: "));

    HashMap<String, String> findUser = new HashMap<String, String>();

    findUser.put("name", user.getName());
    findUser.put("username", user.getUsername());
    findUser.put("email", user.getEmail());
    findUser.put("todos", user.getTodos().toString());

    return findUser;
  }

  @Override
  public HashMap<String, List<String>> findAllEmail() {
    List<User> users = userRepository.findAll();

    HashMap<String, List<String>> findUser = new HashMap<String, List<String>>();
    List<String> emails = new ArrayList<String>();
    List<String> usernames = new ArrayList<String>();

    for (User i : users) {
      emails.add(i.getEmail());
      usernames.add(i.getUsername());
    }

    findUser.put("email", emails);
    findUser.put("username", usernames);

    return findUser;
  }

}