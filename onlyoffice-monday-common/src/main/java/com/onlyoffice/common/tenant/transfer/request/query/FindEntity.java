package com.onlyoffice.common.tenant.transfer.request.query;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FindEntity {
  @Positive private int id;
}
