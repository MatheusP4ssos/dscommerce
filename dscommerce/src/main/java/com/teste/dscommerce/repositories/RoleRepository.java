package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByAuthority(String authority);
}
