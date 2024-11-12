package com.onlyoffice.user.persistence.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {
  private int mondayId;
  private int tenantId;
}
