package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.PaymentDTO;
import com.teste.dscommerce.services.PaymentService;
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
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
    PaymentDTO dto = paymentService.findById(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<PaymentDTO>> findAll(Pageable pageable) {
    Page<PaymentDTO> dto = paymentService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<PaymentDTO> insert(@Valid @RequestBody PaymentDTO dto) {
    PaymentDTO newDto = paymentService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<PaymentDTO> update(@PathVariable Long id, @Valid @RequestBody PaymentDTO dto) {
    PaymentDTO newDto = paymentService.update(id, dto);
    return ResponseEntity.ok(newDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    paymentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}