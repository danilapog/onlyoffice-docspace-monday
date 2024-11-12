package com.onlyoffice.user.exception;

public class ExecutionTimeoutException extends RuntimeException {
  public ExecutionTimeoutException(String message) {
    super(message);
  }

  public ExecutionTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutionTimeoutException(Throwable cause) {
    super(cause);
  }
}
