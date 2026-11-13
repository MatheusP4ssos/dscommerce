package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
