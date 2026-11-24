package com.teste.dscommerce.controllers;

import com.teste.dscommerce.dto.CategoryDTO;
import com.teste.dscommerce.services.CategoryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> findAll() {
    List<CategoryDTO> list = categoryService.findAll();
    return ResponseEntity.ok(list);
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto) {
    CategoryDTO newDto = categoryService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(newDto.getId())
        .toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
