package com.onlyoffice.common.user.transfer.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {
  private String email;
  private String hash;
}
