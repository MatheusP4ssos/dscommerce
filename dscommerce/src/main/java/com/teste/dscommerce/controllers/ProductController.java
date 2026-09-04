package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping(value = "/{id}")
  public ProductDTO findById(@PathVariable Long id) {
    return productService.findById(id);
  }

  @GetMapping
  public Page<ProductDTO> findAll(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size) {
    return productService.findAll(page, size);
  }
}