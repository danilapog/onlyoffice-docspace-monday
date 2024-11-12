package com.onlyoffice.tenant.service.command;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterDocSpace;
import com.onlyoffice.tenant.exception.TenantNotFoundException;
import com.onlyoffice.tenant.persistence.entity.Docspace;
import com.onlyoffice.tenant.persistence.repository.TenantRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicDocSpaceCommandService implements DocSpaceCommandService {
  private final TenantRepository tenantRepository;

  @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
  public void register(RegisterDocSpace command) {
    try {
      MDC.put("tenant_id", String.valueOf(command.getTenantId()));
      MDC.put("docspace_url", command.getUrl());
      log.info("Registering new DocSpace credentials for this tenant");

      var tenant =
          tenantRepository
              .findById(command.getTenantId())
              .orElseThrow(
                  () ->
                      new TenantNotFoundException(
                          String.format(
                              "Could not find tenant with id %d", command.getTenantId())));

      if (!command.getUrl().equals(tenant.getDocspace().getUrl())) {
        tenant.setBoards(Set.of());
        tenant.setDocspace(
            Docspace.builder()
                .url(command.getUrl())
                .adminLogin(command.getAdminLogin())
                .adminHash(command.getAdminHash())
                .build());
        return;
      }

      var docspace = tenant.getDocspace();
      docspace.setAdminLogin(command.getAdminLogin());
      docspace.setAdminHash(command.getAdminHash());
    } finally {
      MDC.clear();
    }
  }
}
