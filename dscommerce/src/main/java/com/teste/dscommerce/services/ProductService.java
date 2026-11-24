package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.dto.ProductMinDTO;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.ProductMapper;
import com.teste.dscommerce.repositories.CategoryRepository;
import com.teste.dscommerce.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

  private final ProductRepository repository;
  private final ProductMapper mapper;
  private final CategoryRepository categoryRepository;


  public ProductService(ProductRepository repository, ProductMapper mapper, CategoryRepository categoryRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));
    return mapper.toDTO(product);
  }

  @Transactional(readOnly = true)
  public Page<ProductMinDTO> findAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    Page<Product> products = repository.findAll(pageable);
    return products.map(mapper::toMinDTO);
  }

  @Transactional(readOnly = true)
  public Page<ProductMinDTO> findByCategory(Long categoryId, int page, int size) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new ResourceNotFoundException("Categoria não encontrada: " + categoryId);
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    Page<Product> products = repository.findByCategory(categoryId, pageable);
    return products.map(mapper::toMinDTO);
  }

  @Transactional
  public ProductDTO insert(ProductDTO dto) {
    Product entity = mapper.toEntity(dto);
    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    try {
      Product entity = repository.getReferenceById(id);
      copyToEntity(dto, entity);
      entity = repository.save(entity);
      return mapper.toDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id + "");
    }
  }

  @Transactional()
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id);
    }
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Falha de integridade referencial");
    }
  }

  private void copyToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());
  }
}

