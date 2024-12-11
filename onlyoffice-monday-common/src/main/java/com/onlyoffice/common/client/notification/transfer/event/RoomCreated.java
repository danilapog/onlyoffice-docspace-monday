package com.onlyoffice.common.client.notification.transfer.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreated implements NotificationEvent {
  @JsonProperty("board_id")
  private long boardId;

  @JsonProperty("tenant_id")
  private long tenantId;

  @JsonGetter("type")
  public String type() {
    return "room_created";
  }
}
