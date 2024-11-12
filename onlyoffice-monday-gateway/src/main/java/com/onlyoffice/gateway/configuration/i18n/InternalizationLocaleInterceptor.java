package com.onlyoffice.gateway.configuration.i18n;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
@RequiredArgsConstructor
public class InternalizationLocaleInterceptor implements HandlerInterceptor {
  private final MessageSourceLocaleService localeService;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws ServletException {
    var localeResolver = RequestContextUtils.getLocaleResolver(request);
    if (localeResolver == null)
      throw new IllegalStateException("Could not find any locale resolver");

    if (localeResolver instanceof AcceptHeaderLocaleResolver resolver)
      localeService.localeContext().setCurrent(resolver.resolveLocale(request));
    else
      throw new IllegalStateException(
          "Registered resolver must be of type AcceptHeaderLocaleResolver");

    return true;
  }
}
