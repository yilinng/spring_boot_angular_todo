package com.example.demo.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;

  private UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    /*
     * Cookie[] cookies = request.getCookies();
     * 
     * System.out.println("getCookie init");
     * 
     * for (Cookie i : cookies) {
     * System.out.println("doFilterInternal number" + 1);
     * 
     * System.out.println(i.getName());
     * System.out.println(i.getValue());
     * }
     */
    // String nameCookie = cookies[1].getValue();
    // System.out.println(cookies);
    // System.out.println(URLDecoder.decode(nameCookie, "utf8"));

    // Cookie cookieName = WebUtils.getCookie(request, "user-id");
    // String cookieValue = cookieName == null ? "no cookie can see." :
    // cookieName.getValue();

    // Optional<String> getCookie = readServletCookie(request, "user-id");

    // if (getCookie.isPresent()) {
    // System.out.println(getCookie);

    // }

    /*
     * if (cookieValue != null) {
     * System.out.println(getCookie);
     * }
     */
    System.out.println("getCookie after");

    // get JWT token from http request
    String token = getTokenFromRequest(request);

    // validate token
    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

      // get username from token
      String username = jwtTokenProvider.getUsername(token);

      // load the user associated with token
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities());

      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {

    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }

    return null;
  }

  // https://reflectoring.io/spring-boot-cookies/
  public Optional<String> readServletCookie(HttpServletRequest request, String name) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> name.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findAny();
  }

}
