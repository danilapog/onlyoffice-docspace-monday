package com.onlyoffice.gateway.transport.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomCommand {
  private long boardId;
  @Positive private long roomId;
  @NotNull private Set<String> users;
}
