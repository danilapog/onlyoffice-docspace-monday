package com.onlyoffice.gateway.security;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {
  private final String X_FORWARDED_FOR = "X-Forwarded-For";

  private final Function<HttpMethod, Supplier<BucketConfiguration>> bucketFactory;
  private final LettuceBasedProxyManager proxyManager;

  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      var method = request.getMethod();
      var bucketConfigurationFactory = bucketFactory.apply(HttpMethod.valueOf(method));
      var client = request.getHeader(X_FORWARDED_FOR);
      if (client == null || client.isBlank()) client = request.getRemoteAddr();
      var bucket =
          proxyManager
              .builder()
              .build(
                  String.format("%s:%s", method, client).getBytes(StandardCharsets.UTF_8),
                  bucketConfigurationFactory);
      var probe = bucket.tryConsumeAndReturnRemaining(1);
      if (!probe.isConsumed()) {
        MDC.put("client", client);
        log.debug("Client has exhausted all tokens");
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return;
      }

      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
