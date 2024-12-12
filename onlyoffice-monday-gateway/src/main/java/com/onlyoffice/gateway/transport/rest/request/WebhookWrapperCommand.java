package com.onlyoffice.gateway.transport.rest.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookWrapperCommand<T extends MondayWebhook> {
  private String type;
  private T data;
}
