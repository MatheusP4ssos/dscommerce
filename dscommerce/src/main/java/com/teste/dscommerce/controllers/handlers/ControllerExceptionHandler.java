package com.teste.dscommerce.controllers.handlers;

import com.teste.dscommerce.controllers.StandardError;
import com.teste.dscommerce.dto.CustomError;
import com.teste.dscommerce.dto.ValidationError;
import com.teste.dscommerce.exceptions.DatabaseException;
import com.teste.dscommerce.exceptions.ResourceNotFoundException;
import com.teste.dscommerce.exceptions.UserEmailNotFoundException;
import com.teste.dscommerce.exceptions.UserNameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = new StandardError(Instant.now(), status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError err = new StandardError(Instant.now(), status.value(), "Falha de integridade referencial", e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomError> argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados inválidos", request.getRequestURI(), null);
    for (FieldError f : e.getBindingResult().getFieldErrors()) {
      err.addError(f.getField(), f.getDefaultMessage());
    }
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UserNameNotFoundException.class)
  public ResponseEntity<StandardError> userNameNotFound(UserNameNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = new StandardError(Instant.now(), status.value(), "Nome não encontrado",
        e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UserEmailNotFoundException.class)
  public ResponseEntity<StandardError> userEmailNotFound(UserEmailNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = new StandardError(Instant.now(), status.value(), "Email não encontrado",
        e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<StandardError> authentication(AuthenticationException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    StandardError err = new StandardError(Instant.now(), status.value(), "Falha na autenticação",
        "Email ou senha inválidos", request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<StandardError> accessDenied(AccessDeniedException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    StandardError err = new StandardError(Instant.now(), status.value(), "Acesso negado",
        e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }
}
