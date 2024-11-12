package com.onlyoffice.tenant.processor;

import com.onlyoffice.tenant.service.remote.BasicOutboxSenderService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledOutboxProcessor implements OutboxProcessor {
  private final BasicOutboxSenderService service;

  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
  public void run() {
    service.process();
  }
}
