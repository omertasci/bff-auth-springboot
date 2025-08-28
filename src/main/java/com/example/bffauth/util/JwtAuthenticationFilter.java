package com.example.bffauth.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    String auth = request.getHeader("Authorization");
    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      if (jwtUtil.validate(token) && "access".equals(jwtUtil.extractType(token))) {
        String username = jwtUtil.extractUsername(token);
        String domain = jwtUtil.extractDomain(token);
        // create simple authentication carrying principal=username,
        // credentials=null, no authorities
        UsernamePasswordAuthenticationToken at =
            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        at.setDetails(domain); // domain can be fetched from details if needed
        SecurityContextHolder.getContext().setAuthentication(at);
      }
    }
    filterChain.doFilter(request, response);
  }
}
