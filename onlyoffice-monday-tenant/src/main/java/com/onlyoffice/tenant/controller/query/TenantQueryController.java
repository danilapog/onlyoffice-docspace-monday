package com.onlyoffice.tenant.controller.query;

import com.fasterxml.jackson.annotation.JsonView;
import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import com.onlyoffice.common.tenant.transfer.response.View;
import com.onlyoffice.tenant.service.query.TenantQueryService;
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
    value = "/tenants",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TenantQueryController {
  private final TenantQueryService queryService;

  @GetMapping("/{tenantId}")
  @RateLimiter(name = "findTenant")
  @JsonView(View.GetTenantView.class)
  public ResponseEntity<TenantCredentials> findTenant(@PathVariable @Positive long tenantId) {
    return ResponseEntity.ok(queryService.find(FindEntity.builder().id(tenantId).build()));
  }
}
