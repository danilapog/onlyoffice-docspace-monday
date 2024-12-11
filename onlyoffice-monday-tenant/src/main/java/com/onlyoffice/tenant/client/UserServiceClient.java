package com.onlyoffice.tenant.client;

import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.tenant.exception.UserServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

// TODO: Distributed caching in v2?
@FeignClient(
    name = "${spring.cloud.feign.client.onlyoffice-user-name}",
    configuration = UserServiceClientConfiguration.class)
public interface UserServiceClient {
  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceRetry")
  @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "findDocSpaceUsersFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable long tenantId, @RequestParam("id") Set<String> ids);

  @GetMapping("/users/{tenantId}")
  @Retry(name = "userServiceRetry")
  @CircuitBreaker(
      name = "userServiceCircuitBreaker",
      fallbackMethod = "findDocSpaceUsersWithTimeoutFallback")
  ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
      @PathVariable long tenantId,
      @RequestParam("id") Set<String> ids,
      @RequestHeader("X-Timeout") int timeout);

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersFallback(
      long tenantId, Set<String> ids, Exception ex) {
    throw new UserServiceException("Could not find DocSpace users", ex);
  }

  default ResponseEntity<DocSpaceUsers> findDocSpaceUsersWithTimeoutFallback(
      long tenantId, Set<String> ids, int timeout, Exception ex) {
    throw new UserServiceException("Could not find DocSpace users with timeout", ex);
  }
}
