package com.onlyoffice.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = {"com.onlyoffice.tenant", "com.onlyoffice.common"})
public class TenantServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(TenantServerApplication.class, args);
  }
}
