package com.onlyoffice.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableMethodSecurity(securedEnabled = true)
@SpringBootApplication(scanBasePackages = {"com.onlyoffice.gateway", "com.onlyoffice.common"})
public class GatewayServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayServerApplication.class, args);
  }
}
