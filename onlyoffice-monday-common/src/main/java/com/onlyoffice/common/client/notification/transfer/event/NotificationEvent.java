package com.onlyoffice.common.client.notification.transfer.event;

import java.io.Serializable;

public interface NotificationEvent extends Serializable {
  String type();
}
