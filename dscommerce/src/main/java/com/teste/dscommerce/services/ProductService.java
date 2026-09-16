package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.ProductMapper;
import com.teste.dscommerce.repositories.ProductRepository;
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

    System.out.println(">>> ID da Entidade no Banco: " + entity.getId());

    ProductDTO result = mapper.toDTO(entity);
    System.out.println(">>> ID do DTO gerado pelo Mapper: " + result.getId());

    return result;
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    Product entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));

    copyToEntity(dto, entity);

    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }

  private void copyToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());
  }
}
