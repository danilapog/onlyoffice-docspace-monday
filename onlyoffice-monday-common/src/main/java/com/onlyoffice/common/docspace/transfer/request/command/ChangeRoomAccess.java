package com.onlyoffice.common.docspace.transfer.request.command;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoomAccess {
  private Set<AccessEntry> invitations;
  @Builder.Default private boolean notify = false;

  @Getter
  @Setter
  @Builder
  public static class AccessEntry {
    private final UserAccess access;
    private final String id;
  }
}
