package com.onlyoffice.user.processor;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import com.onlyoffice.user.service.command.UserCommandService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProcessor {
  private final UserCommandService userCommandService;

  @Bean
  public Consumer<CommandMessage<RegisterUser>> registerUserConsumer() {
    return userCommandService::register;
  }

  @Bean
  public Consumer<CommandMessage<RemoveTenantUsers>> removeTenantUsersConsumer() {
    return userCommandService::removeAll;
  }
}
