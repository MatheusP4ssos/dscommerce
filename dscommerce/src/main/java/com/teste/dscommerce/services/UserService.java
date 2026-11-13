package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.UserDTO;
import com.teste.dscommerce.dto.UserInsertDTO;
import com.teste.dscommerce.entities.Role;
import com.teste.dscommerce.entities.User;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.exceptions.UserEmailNotFoundException;
import com.teste.dscommerce.exceptions.UserNameNotFoundException;
import com.teste.dscommerce.mappers.UserMapper;
import com.teste.dscommerce.repositories.RoleRepository;
import com.teste.dscommerce.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

  private final UserRepository repository;
  private final RoleRepository roleRepository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository, RoleRepository roleRepository, UserMapper mapper,
      PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.roleRepository = roleRepository;
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
  public UserDTO findByEmail(String email) {
    User user = repository.findByEmail(email)
        .orElseThrow(() -> new UserEmailNotFoundException(email));
    return mapper.toDTO(user);
  }

  @Transactional(readOnly = true)
  public Page<UserDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    return repository.findAll(pageable).map(mapper::toDTO);
  }

  @Transactional(readOnly = true)
  public Page<UserDTO> findAllByName(String name, Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
    Page<UserDTO> result = repository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDTO);

    if (result.isEmpty()) {
      throw new UserNameNotFoundException(name);
    }
    return result;
  }

  @Transactional
  public UserDTO insert(UserInsertDTO dto) {
    User entity = mapper.toEntity(dto);
    entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    entity.setRoles(resolveRolesForInsert(dto.getRoles()));
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
    entity.setRoles(toRoles(dto.getRoles()));
  }

  private Set<Role> resolveRolesForInsert(Set<String> authorities) {
    if (!isCurrentUserAdmin() || authorities == null || authorities.isEmpty()) {
      return toRoles(Set.of("ROLE_CLIENT"));
    }
    return toRoles(authorities);
  }

  private boolean isCurrentUserAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
  }

  private Set<Role> toRoles(Set<String> authorities) {
    Set<Role> roles = new HashSet<>();
    if (authorities != null) {
      for (String authority : authorities) {
        Role role = roleRepository.findByAuthority(authority)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado: " + authority));
        roles.add(role);
      }
    }
    return roles;
  }
}

