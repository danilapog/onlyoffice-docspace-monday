package com.onlyoffice.gateway.controller.view.model.settings;

import com.onlyoffice.gateway.controller.view.model.LoginModel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsAdminConfigureModel {
  @Builder.Default
  private SettingsLoginFormModel settingsForm = SettingsLoginFormModel.builder().build();

  @Builder.Default private LoginModel login = LoginModel.builder().build();

  @Builder.Default
  private SettingsConfigureInformationModel information =
      SettingsConfigureInformationModel.builder().build();
}
