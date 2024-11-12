package com.onlyoffice.tenant.service.query;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import jakarta.validation.Valid;

public interface TenantQueryService {
  TenantCredentials find(@Valid FindEntity query);
}
