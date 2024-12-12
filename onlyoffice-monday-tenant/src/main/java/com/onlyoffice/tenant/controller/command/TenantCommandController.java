package com.onlyoffice.tenant.controller.command;

import com.fasterxml.jackson.annotation.JsonView;
import com.onlyoffice.common.tenant.transfer.request.command.RegisterTenant;
import com.onlyoffice.common.tenant.transfer.request.command.RemoveTenant;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import com.onlyoffice.common.tenant.transfer.response.View;
import com.onlyoffice.tenant.service.command.TenantCommandService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// TODO: Add Handlers with X-Timeout
@Validated
@RestController
@RequestMapping(
    value = "/tenants",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TenantCommandController {
  private final TenantCommandService commandService;

  @PostMapping
  @RateLimiter(name = "registerTenant")
  @JsonView(View.RegisterTenantView.class)
  public ResponseEntity<TenantCredentials> registerTenant(
      @RequestBody @Valid RegisterTenant command) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commandService.register(command));
  }

  @DeleteMapping
  @RateLimiter(name = "registerTenant")
  public ResponseEntity<?> removeTenant(@RequestBody @Valid RemoveTenant command) {
    return ResponseEntity.status(
            commandService.remove(command) ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
        .build();
  }
}
