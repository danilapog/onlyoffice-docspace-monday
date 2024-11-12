package com.onlyoffice.common.tenant.transfer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantCredentials {
  @JsonView(View.RegisterTenantView.class)
  private int id;

  @JsonProperty("docspace_url")
  @JsonView(View.GetTenantView.class)
  private String docSpaceUrl;

  @JsonProperty("docspace_email")
  @JsonView(View.GetTenantView.class)
  private String docSpaceLogin;

  @JsonProperty("docspace_hash")
  @JsonView(View.GetTenantView.class)
  private String docSpaceHash;
}
