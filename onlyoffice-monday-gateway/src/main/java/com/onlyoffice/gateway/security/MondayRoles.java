package com.onlyoffice.gateway.security;

import lombok.Getter;

@Getter
public enum MondayRoles {
  GUEST("ROLE_GUEST"),
  VIEWER("ROLE_VIEWER"),
  ROLE_MEMBER("ROLE_MEMBER"),
  ADMIN("ROLE_ADMIN");
  private final String role;

  MondayRoles(String role) {
    this.role = role;
  }
}
