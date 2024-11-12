package com.onlyoffice.notification.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties("camel.component.telegram")
public class TelegramBotConfiguration {
  private String authToken;
  private String chatId;
}
