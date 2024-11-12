package com.onlyoffice.common.tenant.transfer.event;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessKeyRefreshed {
  private int boardId;
  private int tenantId;
}
