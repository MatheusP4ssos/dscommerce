package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.CustomError;
import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.services.ProductService;
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
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> findById(@PathVariable Long id) {

      ProductDTO dto = productService.findById(id);
      return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
    Page<ProductDTO> dto = productService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    return ResponseEntity.ok(dto);
  }


  @PostMapping
  public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
    ProductDTO newDto = productService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
    ProductDTO newDto = productService.update(id, dto);
    return ResponseEntity.ok(newDto);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}