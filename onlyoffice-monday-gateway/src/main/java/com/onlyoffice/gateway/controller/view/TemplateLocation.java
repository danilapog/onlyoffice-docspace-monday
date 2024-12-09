package com.onlyoffice.gateway.controller.view;

import lombok.Getter;

@Getter
public enum TemplateLocation {
  NOT_CONFIGURED_ERROR("pages/errors/configuration"),
  NO_ROOM_ERROR("pages/errors/room"),
  SERVER_ERROR("pages/errors/server"),
  BOARD_ADMIN_CONFIGURE("pages/board/configure"),
  ADMIN_CONFIGURE("pages/settings/configure/admin"),
  ADMIN_LOGIN("pages/settings/login/admin"),
  USER_LOGIN("pages/settings/login/user");
  private final String path;

  TemplateLocation(String path) {
    this.path = path;
  }
}
