package com.onlyoffice.gateway.controller.rest;

import com.onlyoffice.common.client.notification.factory.NotificationPublisherFactory;
import com.onlyoffice.common.client.notification.transfer.event.NotificationEvent;
import com.onlyoffice.common.client.notification.transfer.event.RoomCreated;
import com.onlyoffice.common.tenant.transfer.request.command.RegisterRoom;
import com.onlyoffice.gateway.client.TenantServiceClient;
import com.onlyoffice.gateway.security.MondayAuthenticationPrincipal;
import com.onlyoffice.gateway.transport.rest.request.CreateRoomCommand;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/1.0/rooms")
public class RoomController {
  private final TenantServiceClient tenantService;
  private final Consumer<NotificationEvent> messagePublisher;

  public RoomController(TenantServiceClient tenantService, NotificationPublisherFactory factory) {
    this.tenantService = tenantService;
    this.messagePublisher = factory.getPublisher("notifications");
  }

  @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<?> createRoom(
      @AuthenticationPrincipal MondayAuthenticationPrincipal user,
      @ModelAttribute CreateRoomCommand body) {
    try {
      MDC.put("tenant_id", String.valueOf(user.getAccountId()));
      MDC.put("board_id", String.valueOf(body.getBoardId()));
      MDC.put("user_id", String.valueOf(user.getUserId()));
      if (!tenantService.findTenant(user.getAccountId()).getStatusCode().is2xxSuccessful()) {
        log.warn("Could not find tenant");
        return ResponseEntity.badRequest().build();
      }

      var response =
          tenantService.createRoom(
              RegisterRoom.builder()
                  .boardId(body.getBoardId())
                  .tenantId(user.getAccountId())
                  .roomId(body.getRoomId())
                  .mondayUsers(body.getUsers())
                  .build());

      log.info("Board room has been registered");

      messagePublisher.accept(
          RoomCreated.builder().tenantId(user.getAccountId()).boardId(body.getBoardId()).build());

      log.debug("Room created notification has been sent");

      return ResponseEntity.status(response.getStatusCode().value())
          .header("HX-Refresh", "true")
          .build();
    } finally {
      MDC.clear();
    }
  }
}
