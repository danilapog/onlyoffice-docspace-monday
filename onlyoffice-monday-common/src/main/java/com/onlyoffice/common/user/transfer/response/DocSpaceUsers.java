package com.onlyoffice.common.user.transfer.response;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DocSpaceUsers {
  private Set<String> ids;
}
