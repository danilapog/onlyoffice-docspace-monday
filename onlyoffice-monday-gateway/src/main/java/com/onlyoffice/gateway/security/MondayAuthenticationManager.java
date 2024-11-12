package com.onlyoffice.gateway.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MondayAuthenticationManager implements AuthenticationManager {
  private final List<AuthenticationProvider> providers;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var aclass = authentication.getClass();
    for (var provider : providers) {
      if (provider.supports(aclass)) {
        return provider.authenticate(authentication);
      }
    }

    throw new ProviderNotFoundException(
        String.format(
            "Could not find any appropriate AuthenticationProvider for %s type",
            authentication.getClass().getName()));
  }
}
