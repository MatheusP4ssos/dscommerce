package com.teste.dscommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class OrderItemDTO {

  @NotNull(message = "Campo obrigatório")
  private Long orderId;

  @NotNull(message = "Campo obrigatório")
  private Long productId;

  @Positive(message = "Quantidade deve ser positiva")
  private Integer quantity;

  private String imageUrl;

  @PositiveOrZero(message = "Preço não pode ser negativo")
  private BigDecimal price;

  private String productName;

  public OrderItemDTO() {
  }

  public OrderItemDTO(Long orderId, Long productId, Integer quantity, String imageUrl, BigDecimal price, String productName) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.imageUrl = imageUrl;
    this.price = price;
    this.productName = productName;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
