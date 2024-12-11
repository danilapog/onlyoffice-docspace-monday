package com.onlyoffice.gateway.transport.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionToken {
  @JsonProperty("account_id")
  private long accountId;
}
