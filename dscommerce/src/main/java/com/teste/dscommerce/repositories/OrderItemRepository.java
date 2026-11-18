package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.OrderItem;
import com.teste.dscommerce.entities.OrderItemPK;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

  @EntityGraph(attributePaths = {"id.order", "id.product"})
  Optional<OrderItem> findById(OrderItemPK id);

  @EntityGraph(attributePaths = {"id.order", "id.product"})
  Page<OrderItem> findAll(Pageable pageable);
}
