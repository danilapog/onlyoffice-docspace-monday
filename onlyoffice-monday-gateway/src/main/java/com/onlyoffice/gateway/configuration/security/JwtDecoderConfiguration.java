package com.onlyoffice.gateway.configuration.security;

import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfiguration {
  private final ClientRegistrationRepository clientRegistrationRepository;

  @Bean
  public JwtDecoder jwtDecoder() {
    var bytes =
        clientRegistrationRepository.findByRegistrationId("monday").getClientSecret().getBytes();
    return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(bytes, "HmacSHA256")).build();
  }
}
