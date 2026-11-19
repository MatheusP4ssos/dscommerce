package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @EntityGraph(attributePaths = {"items", "items.id.product", "client", "payment"})
  Optional<Order> findById(Long id);

  @EntityGraph(attributePaths = {"client", "payment"})
  Page<Order> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"client", "payment"})
  Page<Order> findByClientEmail(String email, Pageable pageable);
}
