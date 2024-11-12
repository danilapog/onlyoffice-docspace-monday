package com.onlyoffice.user.controller;

import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import com.onlyoffice.user.service.command.UserCommandService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Timeout header + service timeout
@Slf4j
@Validated
@RestController
@RequestMapping(
    value = "/users",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserCommandController {
  private final UserCommandService commandService;

  @PostMapping
  @RateLimiter(name = "registerUser")
  public ResponseEntity<UserCredentials> registerUser(@RequestBody RegisterUser body) {
    commandService.register(body);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
