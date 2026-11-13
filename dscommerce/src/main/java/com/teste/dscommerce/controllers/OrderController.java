package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.OrderDTO;
import com.teste.dscommerce.dto.OrderInsertDTO;
import com.teste.dscommerce.services.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
    OrderDTO dto = orderService.findById(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<OrderDTO>> findAll(Pageable pageable) {
    Page<OrderDTO> dto = orderService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderInsertDTO dto) {
    OrderDTO newDto = orderService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
    OrderDTO newDto = orderService.update(id, dto);
    return ResponseEntity.ok(newDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderService.delete(id);
    return ResponseEntity.noContent().build();
  }
}