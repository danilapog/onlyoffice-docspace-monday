package com.onlyoffice.common.user.transfer.request.query;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FindUser {
  @Positive private long tenantId;
  @Positive private long mondayId;
}
