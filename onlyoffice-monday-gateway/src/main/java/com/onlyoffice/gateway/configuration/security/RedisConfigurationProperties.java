package com.onlyoffice.gateway.configuration.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("spring.data.redis")
public class RedisConfigurationProperties {
  private int database;
  private String url;
  private String host;
  private int port;
  private String username;
  private String password;
  private SSL ssl;

  @Data
  public static class SSL {
    private boolean enabled;
    private String bundle;
  }
}
