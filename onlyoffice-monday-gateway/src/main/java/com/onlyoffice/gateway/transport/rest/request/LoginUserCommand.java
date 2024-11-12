package com.onlyoffice.gateway.transport.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserCommand {
  @JsonProperty("docspace_user_id")
  private String docSpaceUserId;

  @JsonProperty("docspace_email")
  private String docSpaceEmail;

  @JsonProperty("docspace_hash")
  private String docSpaceHash;
}
