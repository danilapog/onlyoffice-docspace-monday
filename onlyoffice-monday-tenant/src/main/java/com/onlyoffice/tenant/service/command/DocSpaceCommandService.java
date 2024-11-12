package com.onlyoffice.tenant.service.command;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterDocSpace;
import jakarta.validation.Valid;

public interface DocSpaceCommandService {
  void register(@Valid RegisterDocSpace command);
}
