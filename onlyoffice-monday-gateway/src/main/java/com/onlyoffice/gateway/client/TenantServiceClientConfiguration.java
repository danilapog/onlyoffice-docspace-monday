package com.onlyoffice.gateway.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantServiceClientConfiguration {
  @Bean("tenantErrorDecoder")
  public ErrorDecoder errorDecoder() {
    return new TenantServiceClientErrorDecoder();
  }
}
