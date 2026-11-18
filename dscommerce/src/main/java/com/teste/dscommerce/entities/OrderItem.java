package com.teste.dscommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {

  @EmbeddedId
  private OrderItemPK id = new OrderItemPK();
  private Integer quantity;
  @Column(precision = 12, scale = 2)
  private BigDecimal price;

  public OrderItem() {
  }

  public OrderItem(Order order, Product product, Integer quantity, BigDecimal price) {
    id.setOrder(order);
    id.setProduct(product);
    this.quantity = quantity;
    this.price = price;
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

  public BigDecimal getSubTotal() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }

  public Order getOrder() {
    return id.getOrder();
  }

  public void setOrder(Order order) {
    id.setOrder(order);
  }

  public Product getProduct() {
    return id.getProduct();
  }

  public void setProduct(Product product) {
    id.setProduct(product);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItem orderItem)) {
      return false;
    }
    return id != null && id.equals(orderItem.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
