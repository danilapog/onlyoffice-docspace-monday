package com.onlyoffice.common.user.transfer.request.query;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUser {
  @Positive private int tenantId;
  @Positive private int mondayId;
}
