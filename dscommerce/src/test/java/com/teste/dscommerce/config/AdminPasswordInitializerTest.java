package com.teste.dscommerce.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.teste.dscommerce.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(properties = {"security.jwt.ephemeral=true", "seed.admin.password=Senha@123"})
class AdminPasswordInitializerTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void adminPasswordIsOverriddenAfterStartup() {
    String stored = userRepository.findByEmail("admin@dscommerce.com")
        .orElseThrow()
        .getPassword();

    assertTrue(passwordEncoder.matches("Senha@123", stored));
  }
}