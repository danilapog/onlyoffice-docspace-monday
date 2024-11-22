package com.onlyoffice.common.tenant.transfer.request.query;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class FindEntity {
  @Positive private int id;
}
