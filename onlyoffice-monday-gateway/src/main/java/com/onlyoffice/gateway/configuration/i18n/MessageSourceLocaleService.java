package com.onlyoffice.gateway.configuration.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSourceLocaleService implements MessageSourceService {
  private final MessageSource messageSource;

  @Lookup
  public InternalizationLocaleContext localeContext() {
    return null;
  }

  public String getMessage(String code, String... args) {
    return messageSource.getMessage(code, args, localeContext().getCurrent());
  }
}
