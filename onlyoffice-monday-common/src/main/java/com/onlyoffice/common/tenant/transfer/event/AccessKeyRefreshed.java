package com.onlyoffice.common.tenant.transfer.event;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccessKeyRefreshed {
  private long boardId;
  private long tenantId;
}
