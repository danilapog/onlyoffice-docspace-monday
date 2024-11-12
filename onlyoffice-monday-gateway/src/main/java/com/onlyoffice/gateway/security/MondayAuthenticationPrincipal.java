package com.onlyoffice.gateway.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(using = MondayAuthenticationPrincipalDeserializer.class)
public class MondayAuthenticationPrincipal {
  private int userId;
  private int accountId;
  private String slug;
  private boolean isAdmin;
  private boolean isViewOnly;
  private boolean isGuest;
}
