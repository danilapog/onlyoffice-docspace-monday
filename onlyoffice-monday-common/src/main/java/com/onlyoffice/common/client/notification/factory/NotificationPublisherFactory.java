package com.onlyoffice.common.client.notification.factory;

import com.onlyoffice.common.client.notification.transfer.event.NotificationEvent;
import java.util.function.Consumer;

public interface NotificationPublisherFactory {
  <T extends NotificationEvent> Consumer<T> getPublisher(String channel);
}
