package com.onlyoffice.tenant.exception;

public class DocSpaceServiceException extends RuntimeException {
  public DocSpaceServiceException(String message) {
    super(message);
  }

  public DocSpaceServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
