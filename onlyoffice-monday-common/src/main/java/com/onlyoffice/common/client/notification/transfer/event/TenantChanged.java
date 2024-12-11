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
public class TenantChanged implements NotificationEvent {
  @JsonProperty("tenant_id")
  private long tenantId;

  @JsonGetter("type")
  public String type() {
    return "tenant_settings_changed";
  }
}
