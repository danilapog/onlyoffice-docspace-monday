package com.onlyoffice.tenant.processor;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.client.notification.factory.NotificationPublisherFactory;
import com.onlyoffice.common.client.notification.transfer.event.AccessKeyRefreshed;
import com.onlyoffice.common.client.notification.transfer.event.NotificationEvent;
import com.onlyoffice.common.tenant.transfer.request.command.InviteRoomUsers;
import com.onlyoffice.common.tenant.transfer.request.command.RefreshAccessKey;
import com.onlyoffice.tenant.service.remote.BasicDocSpaceRemoteRoomService;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamDocSpaceProcessor implements DocSpaceProcessor {
  private final BasicDocSpaceRemoteRoomService service;
  private final Consumer<NotificationEvent> messagePublisher;

  public StreamDocSpaceProcessor(
      BasicDocSpaceRemoteRoomService service, NotificationPublisherFactory publisherFactory) {
    this.service = service;
    this.messagePublisher = publisherFactory.getPublisher("notifications");
  }

  @Bean
  public Consumer<CommandMessage<RefreshAccessKey>> refreshKeyConsumer() {
    return (msg) -> {
      var event = service.refreshAccessKey(msg);
      log.info("Sending an access key refreshed notification");
      messagePublisher.accept(
          AccessKeyRefreshed.builder()
              .tenantId(event.getTenantId())
              .boardId(event.getBoardId())
              .build());
    };
  }

  @Bean
  public Consumer<CommandMessage<InviteRoomUsers>> inviteUsersConsumer() {
    return service::inviteUsers;
  }
}
