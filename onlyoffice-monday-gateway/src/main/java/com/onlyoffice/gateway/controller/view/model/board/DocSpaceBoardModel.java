package com.onlyoffice.gateway.controller.view.model.board;

import com.onlyoffice.gateway.controller.view.model.LoginModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DocSpaceBoardModel {
  @Builder.Default private LoginModel login = LoginModel.builder().build();

  @Builder.Default
  private DocSpaceBoardManagerModel docSpaceManager = DocSpaceBoardManagerModel.builder().build();

  @Getter
  @Setter
  @Builder
  public static class DocSpaceBoardManagerModel {
    @Builder.Default private String accessKey = "";
    @Builder.Default private long roomId = -1;

    @Builder.Default
    private String notificationText = "Public room is now available for this board";

    @Builder.Default private String welcomeText = "Welcome to DocSpace";

    @Builder.Default
    private String notPublicText =
        "Board room is not public yet. Please login or wait for it to become public";

    @Builder.Default private String unlinkText = "Unlink";
    @Builder.Default private String unlinkRoomHeader = "Oops!";

    @Builder.Default
    private String unlinkRoomText =
        "Apparently the room linked to your board is archived/removed. Do you want to unlink it?";
  }
}
