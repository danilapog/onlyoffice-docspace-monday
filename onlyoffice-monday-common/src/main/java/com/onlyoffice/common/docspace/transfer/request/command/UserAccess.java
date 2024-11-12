package com.onlyoffice.common.docspace.transfer.request.command;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserAccess {
  NONE(0),
  POWER_USER(11);
  private final int code;

  UserAccess(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return this.code;
  }
}
