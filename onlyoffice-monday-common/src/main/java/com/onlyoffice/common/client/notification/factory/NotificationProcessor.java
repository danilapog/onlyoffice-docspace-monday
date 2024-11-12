package com.onlyoffice.common.client.notification.factory;

import org.springframework.data.redis.connection.Message;

public interface NotificationProcessor {
  void onMessage(Message message, byte[] pattern);
}
