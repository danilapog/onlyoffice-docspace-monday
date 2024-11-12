package com.onlyoffice.common.tenant.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterTenant {
  @Positive private int id;
  private int mondayUserId;

  @NotNull @NotBlank @URL private String url;

  @JsonProperty("docspace_user_id")
  private String docSpaceUserId;

  @NotNull
  @NotBlank
  @JsonProperty("email")
  private String adminLogin;

  @NotNull
  @NotBlank
  @JsonProperty("hash")
  private String adminHash;
}
