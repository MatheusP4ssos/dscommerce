package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.ProductDTO;
import com.teste.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}