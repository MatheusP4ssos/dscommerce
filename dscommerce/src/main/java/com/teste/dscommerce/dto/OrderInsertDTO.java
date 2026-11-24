package com.teste.dscommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderInsertDTO {

  private Long clientId;

  @Valid
  @NotEmpty(message = "Campo obrigatório")
  private List<OrderItemInsertDTO> items;

  public OrderInsertDTO() {
  }

  public OrderInsertDTO(Long clientId, List<OrderItemInsertDTO> items) {
    this.clientId = clientId;
    this.items = items;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public List<OrderItemInsertDTO> getItems() {
    return items;
  }

  public void setItems(List<OrderItemInsertDTO> items) {
    this.items = items;
  }

}
