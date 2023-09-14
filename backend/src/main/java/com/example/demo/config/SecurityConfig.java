package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import com.example.demo.security.JwtAuthenticationEntryPoint;
import com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  /*
   * 
   * @Bean
   * public JwtAuthenticationFilter jwtAuthenticationFilter() {
   * return new JwtAuthenticationFilter();
   * }
   */
  // the session policy is set to STATELESS because we’ll use REST endpoints
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // https://stackoverflow.com/questions/72381114/spring-security-upgrading-the-deprecated-websecurityconfigureradapter-in-spring
    // https://stackoverflow.com/questions/64191637/the-method-withdefaults-is-undefined-for-the-type-securityconfiguration
    http.cors(withDefaults()).csrf(csrf -> {
      try {
        csrf.disable()
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));
      } catch (Exception e) {
        e.printStackTrace();
      }
    })
        .sessionManagement(management -> management
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests()
        .antMatchers("/api/auth/login", "/api/auth/signup", "/api/auth/email").permitAll()
        .antMatchers("/api/test/**").permitAll()
        .antMatchers(HttpMethod.GET, "/api/todos/**").permitAll()
        .anyRequest().authenticated();

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}