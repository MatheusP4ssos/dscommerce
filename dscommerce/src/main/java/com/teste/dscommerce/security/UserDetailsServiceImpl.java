package com.teste.dscommerce.security;

import com.teste.dscommerce.entities.User;
import com.teste.dscommerce.repositories.UserRepository;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository repository;

  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getAuthority()))
        .toList();

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .build();
  }
}
