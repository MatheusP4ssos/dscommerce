package com.teste.dscommerce.config;

import com.teste.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@ConditionalOnProperty(name = "seed.admin.password")
public class AdminPasswordInitializer {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final String adminPassword;

  public AdminPasswordInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
      @Value("${seed.admin.password:}") String adminPassword) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.adminPassword = adminPassword;
  }

  @EventListener(ApplicationReadyEvent.class)
  @Transactional
  public void apply() {
    userRepository.findByEmail("admin@dscommerce.com").ifPresent(user -> {
      user.setPassword(passwordEncoder.encode(adminPassword));
      userRepository.save(user);
    });
  }
}