package com.onlyoffice.tenant.exception;

public class OperationExecutionException extends RuntimeException {
  public OperationExecutionException(String message) {
    super(message);
  }

  public OperationExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public OperationExecutionException(Throwable cause) {
    super(cause);
  }
}
