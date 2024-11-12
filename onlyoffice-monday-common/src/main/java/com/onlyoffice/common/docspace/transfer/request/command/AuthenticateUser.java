package com.onlyoffice.common.docspace.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateUser {
  @JsonProperty("UserName")
  private String userName;

  @JsonProperty("PasswordHash")
  private String password;
}
