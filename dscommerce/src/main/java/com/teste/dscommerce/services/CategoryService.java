package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.CategoryDTO;
import com.teste.dscommerce.entities.Category;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.CategoryMapper;
import com.teste.dscommerce.repositories.CategoryRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll() {
    List<Category> result = repository.findAll();
    return result.stream().map(mapper::toDTO).toList();
  }

  @Transactional
  public CategoryDTO insert(CategoryDTO dto) {
    Category entity = mapper.toEntity(dto);
    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Id não encontrado: " + id);
    }
    try {
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Violação de integridade referencial");
    }
  }
}
