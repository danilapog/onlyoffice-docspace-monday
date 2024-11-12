package com.onlyoffice.common.client.notification.transfer.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessKeyRefreshed implements NotificationEvent {
  @JsonProperty("board_id")
  private int boardId;

  @JsonProperty("tenant_id")
  private int tenantId;

  @JsonGetter("type")
  public String type() {
    return "refresh_access_token";
  }
}
