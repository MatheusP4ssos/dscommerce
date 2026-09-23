package com.teste.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

  private List<FieldMessage> errors;

  public ValidationError(Instant timestamp, Integer status, String error, String path,
      List<FieldMessage> errors) {
    super(timestamp, status, error, path);
    this.errors = errors != null ? errors : new ArrayList<>();
  }

  public List<FieldMessage> getErrors() {
    return errors;
  }

  public void setErrors(List<FieldMessage> errors) {
    this.errors = errors;
  }

  public void addError(String fieldName, String message) {
    errors.add(new FieldMessage(fieldName, message));
  }
}
