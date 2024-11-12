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
  ResponseEntity<UserCredentials> findUser(@PathVariable int tenantId, @PathVariable int mondayId);

  @GetMapping("/users/{tenantId}/{mondayId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findUserFallback")
  ResponseEntity<UserCredentials> findUser(
      @PathVariable int tenantId,
      @PathVariable int mondayId,
      @RequestHeader(value = "X-Timeout", defaultValue = "3500") int timeout);

  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findDocSpaceUsersFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable("tenantId") int tenantId, @RequestParam("id") Set<Integer> ids);

  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceQueryRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findDocSpaceUsersFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable("tenantId") int tenantId,
      @RequestParam("id") Set<Integer> ids,
      @RequestHeader(value = "X-Timeout", defaultValue = "3500") int timeout);

  @PostMapping("/users")
  @Retry(name = "userServiceCommandRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "registerUserFallback")
  ResponseEntity<?> registerUser(@RequestBody RegisterUser command);

  default ResponseEntity<UserCredentials> findUserFallback(
      int tenantId, int mondayId, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<UserCredentials> findUserFallback(
      int tenantId, int mondayId, int timeout, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersFallback(
      int tenantId, Set<Integer> ids, Exception ex) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersFallback(
      int tenantId, Set<Integer> ids, int timeout) {
    return ResponseEntity.badRequest().build();
  }

  default ResponseEntity<?> registerUserFallback(RegisterUser command, Exception ex) {
    return ResponseEntity.badRequest().build();
  }
}
