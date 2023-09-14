package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    /*
     * https://stackoverflow.com/questions/26727812/spring-crudrepository-
     * orelsethrow
     */
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

    // System.out.println("userRepository.findByUsernameOrEmail(usernameOrEmail,
    // usernameOrEmail) "
    // + userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail));

    System.out.println("user.getRoles " + user.getRoles());

    Set<GrantedAuthority> authorities = user
        .getRoles()
        .stream()
        .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(),
        authorities);
  }
}