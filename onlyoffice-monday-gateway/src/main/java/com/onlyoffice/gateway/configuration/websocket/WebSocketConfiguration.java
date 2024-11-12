package com.onlyoffice.gateway.configuration.websocket;

import com.onlyoffice.common.client.notification.factory.NotificationProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {
  @Value("${server.origin}")
  private String selfOrigin;

  private final WebSocketHandler webSocketHandler;

  public WebSocketConfiguration(NotificationProcessor notificationProcessor) {
    if (WebSocketHandler.class.isAssignableFrom(notificationProcessor.getClass())) {
      this.webSocketHandler = (WebSocketHandler) notificationProcessor;
      return;
    }
    throw new WebSocketProcessorException(
        "Could not find any suitable NotificationProcessor for WebSocketConfiguration");
  }

  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(webSocketHandler, "/notifications")
        .setAllowedOriginPatterns("https://*.monday.com", selfOrigin);
  }
}
