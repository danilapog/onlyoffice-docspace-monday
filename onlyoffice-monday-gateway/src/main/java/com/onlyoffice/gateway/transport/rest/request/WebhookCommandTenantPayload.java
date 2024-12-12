package com.onlyoffice.gateway.transport.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookCommandTenantPayload implements MondayWebhook {
  @JsonProperty("account_id")
  private long tenantId;
}
