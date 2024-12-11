package com.onlyoffice.common.tenant.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDocSpace {
  @Positive
  @JsonProperty("id")
  private long tenantId;

  @NotNull @NotBlank @URL private String url;

  @NotNull
  @NotBlank
  @JsonProperty("email")
  private String adminLogin;

  @NotNull
  @NotBlank
  @JsonProperty("hash")
  private String adminHash;
}
