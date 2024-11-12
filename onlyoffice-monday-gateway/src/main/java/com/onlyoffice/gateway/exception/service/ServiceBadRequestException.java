package com.onlyoffice.gateway.exception.service;

public class ServiceBadRequestException extends RuntimeException {
  public ServiceBadRequestException(String message) {
    super(message);
  }

  public ServiceBadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
