package com.teste.dscommerce.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class PaymentDTO {

  private Long id;

  @NotNull(message = "Campo obrigatório")
  private Long orderId;

  @NotNull(message = "Campo obrigatório")
  private Instant moment;

  public PaymentDTO() {
  }

  public PaymentDTO(Long id, Long orderId, Instant moment) {
    this.id = id;
    this.orderId = orderId;
    this.moment = moment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Instant getMoment() {
    return moment;
  }

  public void setMoment(Instant moment) {
    this.moment = moment;
  }
}