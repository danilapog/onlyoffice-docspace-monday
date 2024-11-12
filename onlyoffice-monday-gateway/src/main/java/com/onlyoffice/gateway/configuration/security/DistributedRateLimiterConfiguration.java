package com.onlyoffice.gateway.configuration.security;

import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("server.bucket4j")
public class DistributedRateLimiterConfiguration {
  private List<RateLimitProperties> rateLimits;

  @Data
  @Builder
  public static class RateLimitProperties {
    private String method;
    private int capacity;
    private Refill refill;

    @Data
    @Builder
    public static class Refill {
      private int tokens;
      private int period;
      private ChronoUnit timeUnit;
    }
  }
}
