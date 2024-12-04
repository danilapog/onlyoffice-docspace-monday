package com.onlyoffice.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableCaching
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@SpringBootApplication
public class UserServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserServerApplication.class, args);
  }
}
