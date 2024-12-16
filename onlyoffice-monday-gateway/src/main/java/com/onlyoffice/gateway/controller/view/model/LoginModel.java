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

  @Builder.Default
  private String cspError = "Error during initialization. Please check your DocSpace CSP settings";

  @Builder.Default
  private String sizeHeaderError = "Your window is too small to display all page contents";

  @Builder.Default
  private String sizeHeaderText = "Please resize the window or enable full-screen mode";
}
