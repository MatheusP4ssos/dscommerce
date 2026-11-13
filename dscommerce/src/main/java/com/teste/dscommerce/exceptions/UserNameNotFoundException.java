package com.teste.dscommerce.exceptions;

public class UserNameNotFoundException extends RuntimeException {

  public UserNameNotFoundException(String name) {
    super("Nome não encontrado: " + name);
  }
}
