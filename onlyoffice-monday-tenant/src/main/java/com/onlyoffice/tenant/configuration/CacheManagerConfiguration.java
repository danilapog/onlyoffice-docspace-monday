package com.onlyoffice.tenant.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO: Replace with distributed cache in v2?
@Configuration
public class CacheManagerConfiguration {
  @Bean
  public CaffeineCacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(30)).maximumSize(100));
    return cacheManager;
  }
}
