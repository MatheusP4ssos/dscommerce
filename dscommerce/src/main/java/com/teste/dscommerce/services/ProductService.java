package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.CustomError;
import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.ProductMapper;
import com.teste.dscommerce.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class ProductService {

  private final ProductRepository repository;
  private final ProductMapper mapper;

  public ProductService(ProductRepository repository, ProductMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));
    return mapper.toDTO(product);
  }

  @Transactional(readOnly = true)
  public Page<ProductDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    return repository.findAll(pageable).map(mapper::toDTO);
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

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id);
    }
    try {
      repository.deleteById(id);
      repository.flush(); // Força a execução SQL imediatamente para capturar exceções do banco dentro do try-catch
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Falha de integridade referencial");
    }
  }

    private void copyToEntity (ProductDTO dto, Product entity){
      entity.setName(dto.getName());
      entity.setDescription(dto.getDescription());
      entity.setPrice(dto.getPrice());
      entity.setImgUrl(dto.getImgUrl());
    }
  }

