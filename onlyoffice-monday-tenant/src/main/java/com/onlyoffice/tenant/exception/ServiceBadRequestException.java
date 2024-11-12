package com.onlyoffice.tenant.exception;

public class ServiceBadRequestException extends RuntimeException {
  public ServiceBadRequestException(String message) {
    super(message);
  }

  public ServiceBadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
