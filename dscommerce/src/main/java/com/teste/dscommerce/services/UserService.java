package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.UserDTO;
import com.teste.dscommerce.dto.UserInsertDTO;
import com.teste.dscommerce.entities.User;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.UserMapper;
import com.teste.dscommerce.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    User user = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));
    return mapper.toDTO(user);
  }

  @Transactional(readOnly = true)
  public Page<UserDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    return repository.findAll(pageable).map(mapper::toDTO);
  }

  @Transactional
  public UserDTO insert(UserInsertDTO dto) {
    User entity = mapper.toEntity(dto);
    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    try {
      entity = repository.save(entity);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Email já cadastrado");
    }
    return mapper.toDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserDTO dto) {
    try {
      User entity = repository.getReferenceById(id);
      copyToEntity(dto, entity);
      entity = repository.save(entity);
      return mapper.toDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Email já cadastrado");
    }
  }

  @Transactional(propagation = Propagation.SUPPORTS)
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

  private void copyToEntity(UserDTO dto, User entity) {
    entity.setName(dto.getName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone());
    entity.setBirthDate(dto.getBirthDate());
    entity.setRoles(dto.getRoles());
  }
}

