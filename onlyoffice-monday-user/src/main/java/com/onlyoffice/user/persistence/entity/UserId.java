package com.onlyoffice.user.persistence.entity;

import java.io.Serializable;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {
  private long mondayId;
  private long tenantId;
}
