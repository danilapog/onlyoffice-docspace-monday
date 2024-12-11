package com.onlyoffice.common.user.transfer.request.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FindDocSpaceUsers {
  @Positive private long tenantId;
  @NotNull private Set<Long> mondayIds;
}
