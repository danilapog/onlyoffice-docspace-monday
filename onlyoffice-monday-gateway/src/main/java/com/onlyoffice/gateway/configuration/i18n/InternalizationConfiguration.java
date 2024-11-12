package com.onlyoffice.gateway.configuration.i18n;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@RequiredArgsConstructor
public class InternalizationConfiguration implements WebMvcConfigurer {
  @Autowired private final InternalizationLocaleInterceptor localeInterceptor;

  @Bean
  public AcceptHeaderLocaleResolver resolver() {
    var resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }

  @Bean
  @Scope(value = WebApplicationContext.SCOPE_REQUEST)
  public InternalizationLocaleContext localHolder() {
    return new InternalizationLocaleContext();
  }

  @Override
  public void addInterceptors(InterceptorRegistry interceptorRegistry) {
    interceptorRegistry.addInterceptor(localeInterceptor);
  }
}
