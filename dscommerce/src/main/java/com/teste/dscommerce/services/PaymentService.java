package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.PaymentDTO;
import com.teste.dscommerce.entities.Payment;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.PaymentMapper;
import com.teste.dscommerce.repositories.OrderRepository;
import com.teste.dscommerce.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final PaymentRepository repository;
  private final OrderRepository orderRepository;
  private final PaymentMapper mapper;

  public PaymentService(PaymentRepository repository, OrderRepository orderRepository, PaymentMapper mapper) {
    this.repository = repository;
    this.orderRepository = orderRepository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PaymentDTO findById(Long id) {
    Payment entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado: " + id));
    return mapper.toDTO(entity);
  }

  @Transactional(readOnly = true)
  public Page<PaymentDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("moment").descending());
    return repository.findAll(pageable).map(mapper::toDTO);
  }

  @Transactional
  public PaymentDTO insert(PaymentDTO dto) {
    if (!orderRepository.existsById(dto.getOrderId())) {
      throw new ResourceNotFoundException("Pedido não encontrado: " + dto.getOrderId());
    }
    if (repository.existsByOrderId(dto.getOrderId())) {
      throw new DatabaseException("Pagamento já existe para o pedido: " + dto.getOrderId());
    }
    Payment entity = mapper.toEntity(dto);
    entity.setOrder(orderRepository.getReferenceById(dto.getOrderId()));
    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public PaymentDTO update(Long id, PaymentDTO dto) {
    try {
      Payment entity = repository.getReferenceById(id);
      entity.setMoment(dto.getMoment());
      entity = repository.save(entity);
      return mapper.toDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Recurso não encontrado: " + id);
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
}