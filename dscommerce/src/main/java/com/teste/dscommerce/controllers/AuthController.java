package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.LoginDTO;
import com.teste.dscommerce.dto.TokenDTO;
import com.teste.dscommerce.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO dto) {
    return ResponseEntity.ok(authService.login(dto));
  }
}
