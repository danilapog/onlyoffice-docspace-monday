package com.onlyoffice.common.tenant.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRoom {
  @Positive private int tenantId;
  @Positive private int boardId;
  @Positive private int roomId;

  @NotNull
  @JsonProperty("users")
  private Set<String> mondayUsers;
}
