package com.onlyoffice.common.client.notification.transfer.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreated implements NotificationEvent {
  @JsonProperty("board_id")
  private int boardId;

  @JsonProperty("tenant_id")
  private int tenantId;

  @JsonGetter("type")
  public String type() {
    return "room_created";
  }
}
