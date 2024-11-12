package com.onlyoffice.notification.router;

import com.onlyoffice.notification.configuration.TelegramBotConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramRouter extends RouteBuilder {
  private final TelegramBotConfiguration telegramConfiguration;

  public void configure() throws Exception {
    from("direct:sendTelegram")
        .to(
            String.format(
                "telegram:bots?authorizationToken=%s&chatId=%s",
                telegramConfiguration.getAuthToken(), telegramConfiguration.getChatId()))
        .log("Message sent to Telegram group");
  }
}
