package com.teste.dscommerce.services;

import com.teste.dscommerce.dto.OrderItemDTO;
import com.teste.dscommerce.entities.Order;
import com.teste.dscommerce.entities.OrderItem;
import com.teste.dscommerce.entities.OrderItemPK;
import com.teste.dscommerce.entities.Product;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.mappers.OrderItemMapper;
import com.teste.dscommerce.repositories.OrderItemRepository;
import com.teste.dscommerce.repositories.OrderRepository;
import com.teste.dscommerce.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {

  private final OrderItemRepository repository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final OrderItemMapper mapper;

  public OrderItemService(OrderItemRepository repository, OrderRepository orderRepository,
      ProductRepository productRepository, OrderItemMapper mapper) {
    this.repository = repository;
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public OrderItemDTO findById(Long orderId, Long productId) {
    OrderItemPK pk = buildPk(orderId, productId);
    OrderItem item = repository.findById(pk)
        .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
    return mapper.toDTO(item);
  }

  @Transactional(readOnly = true)
  public Page<OrderItemDTO> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("quantity"));
    return repository.findAll(pageable).map(mapper::toDTO);
  }

  @Transactional
  public OrderItemDTO insert(OrderItemDTO dto) {
    if (!orderRepository.existsById(dto.getOrderId())) {
      throw new ResourceNotFoundException("Pedido não encontrado: " + dto.getOrderId());
    }
    if (!productRepository.existsById(dto.getProductId())) {
      throw new ResourceNotFoundException("Produto não encontrado: " + dto.getProductId());
    }
    OrderItem entity = new OrderItem(orderRepository.getReferenceById(dto.getOrderId()),
        productRepository.getReferenceById(dto.getProductId()), dto.getQuantity(), dto.getPrice());
    entity = repository.save(entity);
    return mapper.toDTO(entity);
  }

  @Transactional
  public OrderItemDTO update(Long orderId, Long productId, OrderItemDTO dto) {
    try {
      OrderItemPK pk = buildPk(orderId, productId);
      OrderItem entity = repository.getReferenceById(pk);
      entity.setQuantity(dto.getQuantity());
      entity.setPrice(dto.getPrice());
      entity = repository.save(entity);
      return mapper.toDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Item não encontrado");
    }
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long orderId, Long productId) {
    OrderItemPK pk = buildPk(orderId, productId);
    if (!repository.existsById(pk)) {
      throw new ResourceNotFoundException("Item não encontrado");
    }
    try {
      repository.deleteById(pk);
      repository.flush();
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Falha de integridade referencial");
    }
  }

  private OrderItemPK buildPk(Long orderId, Long productId) {
    Order order = orderRepository.getReferenceById(orderId);
    Product product = productRepository.getReferenceById(productId);
    OrderItemPK pk = new OrderItemPK();
    pk.setOrder(order);
    pk.setProduct(product);
    return pk;
  }
}