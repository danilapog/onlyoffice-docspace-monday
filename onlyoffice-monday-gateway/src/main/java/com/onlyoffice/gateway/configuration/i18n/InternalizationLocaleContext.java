package com.onlyoffice.gateway.configuration.i18n;

import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalizationLocaleContext {
  private Locale current;
}
