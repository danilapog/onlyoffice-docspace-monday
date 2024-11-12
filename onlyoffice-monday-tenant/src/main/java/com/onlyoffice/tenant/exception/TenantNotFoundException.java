package com.onlyoffice.tenant.exception;

public class TenantNotFoundException extends RuntimeException {
  public TenantNotFoundException(String message) {
    super(message);
  }

  public TenantNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public TenantNotFoundException(Throwable cause) {
    super(cause);
  }
}
