package com.onlyoffice.gateway.client;

import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Distributed Caching in v2?
@FeignClient(
    name = "${spring.cloud.feign.client.onlyoffice-user-name}",
    configuration = UserServiceClientConfiguration.class)
public interface UserServiceClient {
  @GetMapping("/users/{tenantId}/{mondayId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findUserFallback")
  ResponseEntity<UserCredentials> findUser(
      @PathVariable long tenantId, @PathVariable long mondayId);

  @GetMapping("/users/{tenantId}/{mondayId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findUserFallback")
  ResponseEntity<UserCredentials> findUser(
      @PathVariable long tenantId,
      @PathVariable long mondayId,
      @RequestHeader(value = "X-Timeout", defaultValue = "3500") int timeout);

  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findDocSpaceUsersFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable("tenantId") long tenantId, @RequestParam("id") Set<Long> ids);

  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findDocSpaceUsersFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable("tenantId") long tenantId,
      @RequestParam("id") Set<Long> ids,
      @RequestHeader(value = "X-Timeout", defaultValue = "3500") int timeout);

  @PostMapping("/users")
  @Retry(name = "userServiceCommandRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "registerUserFallback")
  ResponseEntity<?> registerUser(@RequestBody RegisterUser command);

  default ResponseEntity<UserCredentials> findUserFallback(
      long tenantId, long mondayId, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<UserCredentials> findUserFallback(
      long tenantId, long mondayId, int timeout, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersFallback(
      long tenantId, Set<Long> ids, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersFallback(
      long tenantId, Set<Long> ids, int timeout) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<?> registerUserFallback(RegisterUser command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }
}
