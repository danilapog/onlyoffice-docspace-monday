package com.onlyoffice.tenant.service.command;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterTenant;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import jakarta.validation.Valid;

public interface TenantCommandService {
  TenantCredentials register(@Valid RegisterTenant command);
}
