package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @EntityGraph(attributePaths = "categories")
  Optional<Product> findById(Long id);

  @EntityGraph(attributePaths = "categories")
  Page<Product> findAll(Pageable pageable);

  @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id= :categoryId")
  Page<Product> findByCategory(Long categoryId, Pageable pageable);

}
