package com.onlyoffice.gateway.configuration.i18n;

public interface MessageSourceService {
  String getMessage(String code, String... args);
}
