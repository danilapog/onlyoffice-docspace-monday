package com.onlyoffice.gateway.controller.view.model.settings;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsModalModel {
  @Builder.Default private String confirmActionText = "Confirm your action";

  @Builder.Default
  private String agreementText = "Do you agree you want to change DocSpace portal address?";

  @Builder.Default private String changeText = "Change";
  @Builder.Default private String cancelText = "Cancel";

  @Builder.Default
  private String changeError = "Could not remove current DocSpace data. Please try again later";
}
