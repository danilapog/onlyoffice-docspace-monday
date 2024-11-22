package com.onlyoffice.common.user.transfer;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
  private String details;
}
