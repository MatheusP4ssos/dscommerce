package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.OrderItemDTO;
import com.teste.dscommerce.services.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

  @Autowired
  private OrderItemService orderItemService;

  @GetMapping(value = "/{orderId}/{productId}")
  public ResponseEntity<OrderItemDTO> findById(@PathVariable Long orderId, @PathVariable Long productId) {
    OrderItemDTO dto = orderItemService.findById(orderId, productId);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<OrderItemDTO>> findAll(Pageable pageable) {
    Page<OrderItemDTO> dto = orderItemService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<OrderItemDTO> insert(@Valid @RequestBody OrderItemDTO dto) {
    OrderItemDTO newDto = orderItemService.insert(dto);
    return ResponseEntity.ok(newDto);
  }

  @PutMapping(value = "/{orderId}/{productId}")
  public ResponseEntity<OrderItemDTO> update(@PathVariable Long orderId, @PathVariable Long productId,
      @Valid @RequestBody OrderItemDTO dto) {
    OrderItemDTO newDto = orderItemService.update(orderId, productId, dto);
    return ResponseEntity.ok(newDto);
  }

  @DeleteMapping(value = "/{orderId}/{productId}")
  public ResponseEntity<Void> delete(@PathVariable Long orderId, @PathVariable Long productId) {
    orderItemService.delete(orderId, productId);
    return ResponseEntity.noContent().build();
  }
}