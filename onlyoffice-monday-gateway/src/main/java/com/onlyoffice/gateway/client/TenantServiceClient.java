package com.onlyoffice.gateway.client;

import com.onlyoffice.common.tenant.transfer.request.command.*;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Distributed Caching in v2?
@FeignClient(
    name = "${spring.cloud.feign.client.onlyoffice-tenant-name}",
    configuration = TenantServiceClientConfiguration.class,
    fallbackFactory = TenantServiceClientFallbackFactory.class)
public interface TenantServiceClient {
  @PostMapping("/tenants")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<TenantCredentials> createTenant(@RequestBody RegisterTenant command);

  @GetMapping("/tenants/{tenantId}")
  @Retry(name = "tenantServiceQueryRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<TenantCredentials> findTenant(@PathVariable long tenantId);

  @GetMapping("/tenants/boards/{boardId}")
  @Retry(name = "tenantServiceQueryRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<BoardInformation> findBoard(@PathVariable long boardId);

  @PostMapping("/tenants/boards/room")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<?> createRoom(@RequestBody RegisterRoom command);

  @DeleteMapping("/tenants/boards/room")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<?> removeRoom(@RequestBody RemoveRoom command);

  @DeleteMapping("/tenants")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<?> removeTenant(@RequestBody RemoveTenant command);

  @PostMapping("/tenants/docspace")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker")
  ResponseEntity<?> updateDocSpace(@RequestBody RegisterDocSpace command);
}
