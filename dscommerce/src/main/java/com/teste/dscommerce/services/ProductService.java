package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));
    return new ProductDTO(product);
  }
}
