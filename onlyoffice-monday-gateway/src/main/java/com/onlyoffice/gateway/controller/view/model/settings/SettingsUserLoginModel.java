package com.onlyoffice.gateway.controller.view.model.settings;

import com.onlyoffice.gateway.controller.view.model.LoginModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SettingsUserLoginModel {
  @Builder.Default
  private SettingsLoginFormModel settingsForm = SettingsLoginFormModel.builder().build();

  @Builder.Default private LoginModel login = LoginModel.builder().build();

  @Builder.Default
  private SettingsConfigureInformationModel information =
      SettingsConfigureInformationModel.builder().build();

  @Builder.Default private SettingsModalModel modal = SettingsModalModel.builder().build();
}
