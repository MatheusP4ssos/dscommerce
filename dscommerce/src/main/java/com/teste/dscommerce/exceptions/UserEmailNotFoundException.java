package com.teste.dscommerce.exceptions;

public class UserEmailNotFoundException extends RuntimeException {

  public UserEmailNotFoundException(String email) {
    super("Email não encontrado: " + email);
  }
}
