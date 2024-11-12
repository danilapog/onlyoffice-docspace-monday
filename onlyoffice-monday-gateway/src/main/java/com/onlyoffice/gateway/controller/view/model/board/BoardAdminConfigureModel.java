package com.onlyoffice.gateway.controller.view.model.board;

import com.onlyoffice.gateway.controller.view.model.LoginModel;
import com.onlyoffice.gateway.controller.view.model.settings.SettingsConfigureInformationModel;
import com.onlyoffice.gateway.controller.view.model.settings.SettingsLoginFormModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardAdminConfigureModel {
  @Builder.Default
  private SettingsLoginFormModel settingsForm = SettingsLoginFormModel.builder().build();

  @Builder.Default private LoginModel login = LoginModel.builder().build();

  @Builder.Default
  private SettingsConfigureInformationModel information =
      SettingsConfigureInformationModel.builder().build();
}
