package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
