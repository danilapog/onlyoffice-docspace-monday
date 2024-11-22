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
  private int mondayId;
  private int tenantId;
}
