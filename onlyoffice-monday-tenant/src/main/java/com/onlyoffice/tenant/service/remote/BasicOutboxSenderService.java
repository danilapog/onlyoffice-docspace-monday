package com.onlyoffice.tenant.service.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.tenant.transfer.request.command.InviteRoomUsers;
import com.onlyoffice.common.tenant.transfer.request.command.RefreshAccessKey;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import com.onlyoffice.tenant.persistence.entity.Outbox;
import com.onlyoffice.tenant.persistence.entity.OutboxType;
import com.onlyoffice.tenant.persistence.repository.OutboxRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicOutboxSenderService implements OutboxSenderService {
  private final ObjectMapper mapper;
  private final StreamBridge bridge;

  private final OutboxRepository repository;

  @Transactional
  public void process() {
    var outboxList = repository.findTop25ByOrderByCreatedAtAsc();
    var processedOutboxes = new ArrayList<Outbox>();
    for (var outbox : outboxList) {
      try {
        MDC.put("outbox_id", outbox.getId());
        MDC.put("outbox_type", outbox.getType().name());
        log.info("Sending an outbox payload");

        bridge.send(getBinding(outbox.getType()), getBindingPayload(outbox));
        processedOutboxes.add(outbox);
      } catch (JsonProcessingException e) {
        log.error("Could not process a JSON outbox entry", e);
        processedOutboxes.add(outbox);
      } catch (Exception e) {
        log.error("Unexpected error processing outbox", e);
      } finally {
        MDC.clear();
      }
    }

    repository.deleteAll(processedOutboxes);
  }

  private String getBinding(OutboxType type) {
    return switch (type) {
      case REFRESH -> "refreshKeyConsumer-out-0";
      case INVITE -> "inviteUsersConsumer-out-0";
      case CREATE_USER_ON_INITIALIZATION -> "registerUserConsumer-out-0";
      case REMOVE_TENANT_USERS -> "removeTenantUsersConsumer-out-0";
    };
  }

  private CommandMessage<?> getBindingPayload(Outbox outbox) throws JsonProcessingException {
    return switch (outbox.getType()) {
      case REFRESH ->
          mapper.readValue(
              outbox.getPayload(), new TypeReference<CommandMessage<RefreshAccessKey>>() {});
      case INVITE ->
          mapper.readValue(
              outbox.getPayload(), new TypeReference<CommandMessage<InviteRoomUsers>>() {});
      case CREATE_USER_ON_INITIALIZATION ->
          mapper.readValue(
              outbox.getPayload(), new TypeReference<CommandMessage<RegisterUser>>() {});
      case REMOVE_TENANT_USERS ->
          mapper.readValue(
              outbox.getPayload(), new TypeReference<CommandMessage<RemoveTenantUsers>>() {});
    };
  }
}
