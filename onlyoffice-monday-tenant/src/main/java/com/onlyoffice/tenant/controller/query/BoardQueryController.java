package com.onlyoffice.tenant.controller.query;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.tenant.service.query.BoardQueryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Add Handlers with X-Timeout
@Validated
@RestController
@RequestMapping(
    value = "/tenants/boards",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class BoardQueryController {
  private final BoardQueryService queryService;

  @GetMapping("/{boardId}")
  @RateLimiter(name = "findBoard")
  public ResponseEntity<BoardInformation> findBoard(@PathVariable @Positive long boardId) {
    return ResponseEntity.ok(queryService.find(FindEntity.builder().id(boardId).build()));
  }
}
