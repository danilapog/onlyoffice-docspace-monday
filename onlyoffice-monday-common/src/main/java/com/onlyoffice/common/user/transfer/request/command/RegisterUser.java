package com.onlyoffice.common.user.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterUser implements Serializable {
  @Positive private int mondayId;
  @Positive private int tenantId;

  @NotNull
  @NotBlank
  @JsonProperty("docspace_id")
  private String docSpaceId;

  @NotNull @NotBlank @Email private String email;
  @NotNull @NotBlank private String hash;
}
