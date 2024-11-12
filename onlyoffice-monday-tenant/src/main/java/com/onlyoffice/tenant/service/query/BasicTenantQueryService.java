package com.onlyoffice.tenant.service.query;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import com.onlyoffice.tenant.exception.TenantNotFoundException;
import com.onlyoffice.tenant.persistence.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicTenantQueryService implements TenantQueryService {
  private final TenantRepository tenantRepository;

  public TenantCredentials find(FindEntity query) {
    try {
      MDC.put("tenant_id", String.valueOf(query.getId()));
      log.info("Trying to find tenant credentials by tenant id");

      var tenant =
          tenantRepository
              .findById(query.getId())
              .orElseThrow(
                  () ->
                      new TenantNotFoundException(
                          String.format("Could not find tenant with id %d", query.getId())));

      return TenantCredentials.builder()
          .id(tenant.getId())
          .docSpaceUrl(tenant.getDocspace().getUrl())
          .docSpaceLogin(tenant.getDocspace().getAdminLogin())
          .docSpaceHash(tenant.getDocspace().getAdminHash())
          .build();
    } finally {
      MDC.clear();
    }
  }
}
