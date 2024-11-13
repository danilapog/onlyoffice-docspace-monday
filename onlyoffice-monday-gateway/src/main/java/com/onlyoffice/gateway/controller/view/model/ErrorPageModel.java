package com.onlyoffice.gateway.controller.view.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorPageModel {
  @Builder.Default private String refresh = "";
  @Builder.Default private ErrorText error = ErrorText.builder().build();
  @Builder.Default private LoginModel login = LoginModel.builder().build();

  @Getter
  @Setter
  @Builder
  public static class ErrorText {
    private String header;
    private String subtext;
  }
}
