package com.onlyoffice.gateway.controller.view.model.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SettingsLoginFormModel {
  @Builder.Default private String loginText = "Login";
  @Builder.Default private String changeText = "Change DocSpace";

  @Builder.Default
  private SettingsLoginFormFields fields = SettingsLoginFormFields.builder().build();

  @Getter
  @Setter
  @Builder
  public static class SettingsLoginFormFields {
    @Builder.Default private String docSpaceLabel = "DocSpace Server Address";
    @Builder.Default private String docSpacePlaceholder = "Please enter DocSpace address";
    @Builder.Default private String emailLabel = "DocSpace Email";
    @Builder.Default private String emailPlaceholder = "Please enter DocSpace email";
    @Builder.Default private String passwordLabel = "DocSpace Password";
    @Builder.Default private String passwordPlaceholder = "Please enter DocSpace password";
  }
}
