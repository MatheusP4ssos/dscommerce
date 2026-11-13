package com.teste.dscommerce.dto;

import com.teste.dscommerce.entities.OrderStatus;
import java.time.Instant;
import java.util.List;

public class OrderDTO {
private Long id;
private Instant moment;
private OrderStatus orderStatus;
private Long clientId;
private String clientName;
private List<OrderItemDTO> items;
private PaymentDTO payment;
private Double total;

public OrderDTO() {
}

public OrderDTO(Long id, Instant moment, OrderStatus orderStatus, Long clientId, String clientName, List<OrderItemDTO> items, PaymentDTO payment, Double total) {
    this.id = id;
    this.moment = moment;
    this.orderStatus = orderStatus;
    this.clientId = clientId;
    this.clientName = clientName;
    this.items = items;
    this.payment = payment;
    this.total = total;
}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getMoment() {
    return moment;
  }

  public void setMoment(Instant moment) {
    this.moment = moment;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public List<OrderItemDTO> getItems() {
    return items;
  }

  public void setItems(List<OrderItemDTO> items) {
    this.items = items;
  }

  public PaymentDTO getPayment() {
    return payment;
  }

  public void setPayment(PaymentDTO payment) {
    this.payment = payment;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }
}