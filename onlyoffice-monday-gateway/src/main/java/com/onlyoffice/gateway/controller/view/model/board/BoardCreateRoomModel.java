package com.onlyoffice.gateway.controller.view.model.board;

import com.onlyoffice.gateway.controller.view.model.LoginModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardCreateRoomModel {
  private LoginModel login;
  private BoardCreateRoomInformationModel creationInformation;
  @Builder.Default private String roomsQuotaError = "You are out of rooms quota";
  @Builder.Default private String timeoutError = "Operation timed out";
  @Builder.Default private String operationError = "Could not perform current operation";

  @Getter
  @Setter
  @Builder
  public static class BoardCreateRoomInformationModel {
    @Builder.Default private String welcomeText = "Welcome to DocSpace Board!";
    @Builder.Default private String createText = "Please create the room";
    @Builder.Default private String buttonText = "Create room";

    @Builder.Default
    private String noPermissionsText = "Sorry, you don't have enough permissions to create a room";
  }
}
