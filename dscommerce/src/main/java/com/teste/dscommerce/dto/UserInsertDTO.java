package com.teste.dscommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserInsertDTO extends UserDTO {

  @NotBlank(message = "Campo obrigatório")
  @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
  private String password;

  public UserInsertDTO() {
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}