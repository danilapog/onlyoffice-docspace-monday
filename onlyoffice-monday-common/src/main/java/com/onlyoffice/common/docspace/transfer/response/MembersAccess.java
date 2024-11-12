package com.onlyoffice.common.docspace.transfer.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembersAccess {
  private List<MemberAccess> members;

  @Getter
  @Setter
  public static class MemberAccess {
    private boolean canEditAccess;
  }
}
