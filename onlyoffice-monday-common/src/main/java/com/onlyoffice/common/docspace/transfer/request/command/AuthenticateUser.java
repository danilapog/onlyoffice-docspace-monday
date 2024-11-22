package com.onlyoffice.common.docspace.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class AuthenticateUser {
  @JsonProperty("UserName")
  private String userName;

  @JsonProperty("PasswordHash")
  private String password;
}
