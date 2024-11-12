package com.onlyoffice.common.docspace.transfer.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomLink {
  private int access;
  private Share sharedTo;

  @Getter
  @Setter
  public static class Share {
    private String requestToken;
  }
}
