package com.teste.dscommerce.repositories;

import com.teste.dscommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
