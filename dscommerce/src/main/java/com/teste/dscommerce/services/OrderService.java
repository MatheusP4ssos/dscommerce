package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.OrderDTO;
import com.teste.dscommerce.dto.OrderInsertDTO;
import com.teste.dscommerce.dto.OrderItemInsertDTO;
import com.teste.dscommerce.entities.Order;
import com.teste.dscommerce.entities.OrderItem;
import com.teste.dscommerce.entities.OrderStatus;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.entities.User;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.OrderMapper;
import com.teste.dscommerce.repositories.OrderRepository;
import com.teste.dscommerce.repositories.ProductRepository;
import com.teste.dscommerce.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository repository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderMapper mapper;

  public OrderService(OrderRepository repository, UserRepository userRepository,
      ProductRepository productRepository, OrderMapper mapper) {
    this.repository = repository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public OrderDTO findById(Long id) {
    Order order = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id));
    if (!isCurrentUserAdmin() && !order.getClient().getEmail().equals(currentUsername())) {
      throw new AccessDeniedException("Acesso negado a este pedido");
    }
    return mapper.toDTO(order);
  }

  @Transactional(readOnly = true)
  public Page<OrderDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("moment").descending());
    if (isCurrentUserAdmin()) {
      return repository.findAll(pageable).map(mapper::toDTO);
    }
    return repository.findByClientEmail(currentUsername(), pageable).map(mapper::toDTO);
  }

  @Transactional
  public OrderDTO insert(OrderInsertDTO dto) {
    Long clientId = resolveClientId(dto.getClientId());
    if (!userRepository.existsById(clientId)) {
      throw new ResourceNotFoundException("Cliente não encontrado: " + clientId);
    }
    Order entity = new Order();
    entity.setMoment(Instant.now());
    entity.setStatus(OrderStatus.WAITING_PAYMENT);
    entity.setClient(userRepository.getReferenceById(clientId));
    for (OrderItemInsertDTO itemDto : dto.getItems()) {
      Product product = productRepository.findById(itemDto.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Produto não encontrado: " + itemDto.getProductId()));
      entity.getItems().add(new OrderItem(entity, product, itemDto.getQuantity(),
          product.getPrice()));
    }
    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public OrderDTO update(Long id, OrderDTO dto) {
    try {
      Order entity = repository.getReferenceById(id);
      entity.setMoment(dto.getMoment());
      entity.setStatus(dto.getOrderStatus());
      entity = repository.save(entity);
      return mapper.toDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id);
    }
  }

  private Long resolveClientId(Long requestedClientId) {
    if (isCurrentUserAdmin()) {
      if (requestedClientId == null) {
        throw new ResourceNotFoundException("Cliente não informado");
      }
      return requestedClientId;
    }
    User user = userRepository.findByEmail(currentUsername())
        .orElseThrow(() -> new ResourceNotFoundException("Cliente autenticado não encontrado"));
    if (requestedClientId != null && !requestedClientId.equals(user.getId())) {
      throw new AccessDeniedException("Não é permitido criar pedido para outro cliente");
    }
    return user.getId();
  }

  private boolean isCurrentUserAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
  }

  private String currentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication == null ? null : authentication.getName();
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
}