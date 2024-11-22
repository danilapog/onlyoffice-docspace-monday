package com.onlyoffice.user.service.command;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UserCommandService {
  void register(@Valid @NotNull RegisterUser payload);

  void register(@Valid @NotNull CommandMessage<RegisterUser> command);

  void removeAll(@Valid @NotNull CommandMessage<RemoveTenantUsers> command);
}
