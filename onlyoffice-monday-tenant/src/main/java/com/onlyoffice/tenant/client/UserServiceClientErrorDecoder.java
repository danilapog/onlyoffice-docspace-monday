package com.onlyoffice.tenant.client;

import com.onlyoffice.tenant.exception.ServiceBadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class UserServiceClientErrorDecoder implements ErrorDecoder {
  private final ErrorDecoder defaultErrorDecoder = new Default();

  public Exception decode(String methodKey, Response response) {
    if (response.status() == 400)
      return new ServiceBadRequestException("Could not perform operation due to BadRequest status");
    return defaultErrorDecoder.decode(methodKey, response);
  }
}
