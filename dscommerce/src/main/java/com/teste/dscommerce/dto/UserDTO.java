package com.teste.dscommerce.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UserDTO {

  private Long id;
  @NotBlank
  @Size(min = 3, max = 80, message = "O nome deve ter entre 3 e 80 caracteres")
  private String name;
  @NotBlank
  @Email(message = "O email deve ser válido")
  private String email;
  @NotBlank
  @Pattern(regexp = "^\\(?([1-9]{2})\\)?[-. ]?([2-9][0-9]{3,4})[-. ]?([0-9]{4})$",
      message = "O telefone deve conter apenas números e ter 10 ou 11 dígitos")
  private String phone;
  @NotNull
  @Past(message = "A data de nascimento deve ser no passado")
  private LocalDate birthDate;
  private String roles;

  public UserDTO() {
  }

  public UserDTO(Long id, String name, String email, String phone, LocalDate birthDate,
      String roles) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.birthDate = birthDate;
    this.roles = roles;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }
}