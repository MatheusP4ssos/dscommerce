package com.teste.dscommerce.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProductDTO {

  private Long id;
  @Size(min = 3, max = 80, message = "O nome do produto deve ter entre 3 e 80 caracteres")
  @NotBlank(message = "Campo obrigatório")
  private String name;

  @Size(min = 10, message = "A descrição do produto deve ter no mínimo 10 caracteres")
  private String description;
  @Positive(message = "O preço do produto deve ser positivo")
  @Digits(integer = 10, fraction = 2, message = "O preço deve ter no máximo 10 dígitos inteiros e 2 decimais")
  private BigDecimal price;
  private String imgUrl;
  @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
  private Set<String> categories = new LinkedHashSet<>();

  public ProductDTO() {
  }

  public ProductDTO(Long id, String name, String description, BigDecimal price, String imgUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgUrl = imgUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Set<String> getCategories() {
    return categories;
  }

  public void setCategories(Set<String> categories) {
    this.categories = categories;
  }
}
