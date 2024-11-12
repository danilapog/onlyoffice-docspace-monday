package com.onlyoffice.gateway.security;

import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MondayAuthentication implements Authentication {
  private final MondayAuthenticationPrincipal principal;
  private final String signature;
  private boolean isAuthenticated;

  private String getRole() {
    if (principal.isViewOnly()) {
      return MondayRoles.VIEWER.getRole();
    } else if (principal.isAdmin()) {
      return MondayRoles.ADMIN.getRole();
    } else if (principal.isGuest()) {
      return MondayRoles.GUEST.getRole();
    } else {
      return MondayRoles.ROLE_MEMBER.getRole();
    }
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(getRole()));
  }

  public Object getCredentials() {
    return signature;
  }

  public Object getDetails() {
    return getRole();
  }

  public Object getPrincipal() {
    return principal;
  }

  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.isAuthenticated = isAuthenticated;
  }

  public String getName() {
    return String.valueOf(principal.getUserId());
  }
}
