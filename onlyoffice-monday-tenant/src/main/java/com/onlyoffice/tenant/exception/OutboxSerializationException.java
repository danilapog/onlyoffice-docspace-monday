package com.onlyoffice.tenant.exception;

public class OutboxSerializationException extends RuntimeException {
  public OutboxSerializationException(String message) {
    super(message);
  }

  public OutboxSerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutboxSerializationException(Throwable cause) {
    super(cause);
  }
}
