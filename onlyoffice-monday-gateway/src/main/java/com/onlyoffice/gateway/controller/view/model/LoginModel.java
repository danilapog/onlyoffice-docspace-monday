package com.onlyoffice.gateway.controller.view.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginModel {
  @Builder.Default private String url = "";
  @Builder.Default private String email = "";
  @Builder.Default private String hash = "";
  @Builder.Default private String accessText = "Monday requests access to your ONLYOFFICE DocSpace";
  @Builder.Default private String addressText = "docspace.server.info";
  @Builder.Default private String success = "Login successful";
  @Builder.Default private String error = "Login failed";
}
