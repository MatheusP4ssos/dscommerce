package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Page<Order> findByClientEmail(String email, Pageable pageable);
}