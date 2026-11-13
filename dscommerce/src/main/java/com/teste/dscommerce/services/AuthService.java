package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.LoginDTO;
import com.teste.dscommerce.dto.TokenDTO;
import com.teste.dscommerce.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public TokenDTO login(LoginDTO dto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    return jwtService.generateToken(authentication);
  }
}
