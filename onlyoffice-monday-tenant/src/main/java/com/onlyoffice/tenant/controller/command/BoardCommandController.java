package com.onlyoffice.tenant.controller.command;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterRoom;
import com.onlyoffice.tenant.client.UserServiceClient;
import com.onlyoffice.tenant.service.command.BoardCommandService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Add Handlers with X-Timeout
@Validated
@RestController
@RequestMapping(
    value = "/tenants/boards",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class BoardCommandController {
  private final UserServiceClient userService;
  private final BoardCommandService commandService;

  @PostMapping("/room")
  @RateLimiter(name = "registerRoom")
  public ResponseEntity<?> registerRoom(@RequestBody @Valid RegisterRoom command) {
    var users = userService.findDocSpaceUsers(command.getTenantId(), command.getMondayUsers());
    if (!users.getStatusCode().is2xxSuccessful() || users.getBody() == null)
      return ResponseEntity.badRequest().build();

    commandService.register(command, users.getBody().getIds());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
