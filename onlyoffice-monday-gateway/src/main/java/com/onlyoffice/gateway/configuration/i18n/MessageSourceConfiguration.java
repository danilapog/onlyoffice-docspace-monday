package com.onlyoffice.gateway.configuration.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {
  @Bean
  public MessageSource messageSource() {
    var messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("lang/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
