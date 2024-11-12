package com.onlyoffice.user.service.command;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import jakarta.validation.Valid;

public interface UserCommandService {
  void register(@Valid RegisterUser payload);

  void register(@Valid CommandMessage<RegisterUser> command);

  void removeAll(@Valid CommandMessage<RemoveTenantUsers> command);
}
