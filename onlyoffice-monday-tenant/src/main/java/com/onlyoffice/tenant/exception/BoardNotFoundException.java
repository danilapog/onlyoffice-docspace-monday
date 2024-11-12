package com.onlyoffice.tenant.exception;

public class BoardNotFoundException extends RuntimeException {
  public BoardNotFoundException(String message) {
    super(message);
  }

  public BoardNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public BoardNotFoundException(Throwable cause) {
    super(cause);
  }
}
