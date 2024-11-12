package com.onlyoffice.common.tenant.transfer.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
  private String details;
}
