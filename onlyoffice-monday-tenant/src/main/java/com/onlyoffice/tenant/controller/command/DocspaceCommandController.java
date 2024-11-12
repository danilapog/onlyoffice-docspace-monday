package com.onlyoffice.tenant.controller.command;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterDocSpace;
import com.onlyoffice.tenant.service.command.DocSpaceCommandService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// TODO: Add Handlers with X-Timeout
@Validated
@RestController
@RequestMapping(
    value = "/tenants/docspace",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class DocspaceCommandController {
  private final DocSpaceCommandService commandService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @RateLimiter(name = "registerDocSpace")
  public void registerDocSpace(@RequestBody @Valid RegisterDocSpace command) {
    commandService.register(command);
  }
}
