package com.onlyoffice.gateway.client;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterDocSpace;
import com.onlyoffice.common.tenant.transfer.request.command.RegisterRoom;
import com.onlyoffice.common.tenant.transfer.request.command.RegisterTenant;
import com.onlyoffice.common.tenant.transfer.request.command.RemoveRoom;
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
    configuration = TenantServiceClientConfiguration.class)
public interface TenantServiceClient {
  @PostMapping("/tenants")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "createTenantFallback")
  ResponseEntity<TenantCredentials> createTenant(@RequestBody RegisterTenant command);

  @GetMapping("/tenants/{tenantId}")
  @Retry(name = "tenantServiceQueryRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "findTenantFallback")
  ResponseEntity<TenantCredentials> findTenant(@PathVariable long tenantId);

  @GetMapping("/tenants/boards/{boardId}")
  @Retry(name = "tenantServiceQueryRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "findBoardFallback")
  ResponseEntity<BoardInformation> findBoard(@PathVariable long boardId);

  @PostMapping("/tenants/boards/room")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "createRoomFallback")
  ResponseEntity<?> createRoom(@RequestBody RegisterRoom command);

  @DeleteMapping("/tenants/boards/room")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "removeRoomFallback")
  ResponseEntity<?> removeRoom(@RequestBody RemoveRoom command);

  @PostMapping("/tenants/docspace")
  @Retry(name = "tenantServiceCommandRetry")
  @CircuitBreaker(name = "tenantServiceCircuitBreaker", fallbackMethod = "updateDocSpaceFallback")
  ResponseEntity<?> updateDocSpace(@RequestBody RegisterDocSpace command);

  default ResponseEntity<TenantCredentials> createTenantFallback(
      RegisterTenant command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<TenantCredentials> findTenantFallback(long tenantId, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<BoardInformation> findBoardFallback(long boardId, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<?> createRoomFallback(RegisterRoom command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<?> removeRoomFallback(RemoveRoom command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<?> updateDocSpaceFallback(RegisterDocSpace command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }
}
