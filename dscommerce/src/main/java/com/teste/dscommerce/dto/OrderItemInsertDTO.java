package com.teste.dscommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemInsertDTO {

  @NotNull(message = "Campo obrigatório")
  private Long productId;
  @Positive(message = "Deve ser maior que zero")
  private Integer quantity;

  public OrderItemInsertDTO() {

  }

  public Long getProductId() {
    return productId;
  }
  public void setProductId(Long productId) {
    this.productId = productId;
  }
  public Integer getQuantity() {
    return quantity;
  }
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

}
