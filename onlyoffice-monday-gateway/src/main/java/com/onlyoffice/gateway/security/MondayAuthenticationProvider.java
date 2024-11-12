package com.onlyoffice.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MondayAuthenticationProvider implements AuthenticationProvider {
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    authentication.setAuthenticated(true);
    return authentication;
  }

  public boolean supports(Class<?> authentication) {
    return authentication.equals(MondayAuthentication.class);
  }
}
