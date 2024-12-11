package com.onlyoffice.tenant.persistence.repository;

import com.onlyoffice.tenant.persistence.entity.Docspace;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocspaceRepository extends JpaRepository<Docspace, String> {
  Optional<Docspace> findByTenantId(long tenantId);
}
