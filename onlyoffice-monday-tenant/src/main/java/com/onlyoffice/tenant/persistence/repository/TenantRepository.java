package com.onlyoffice.tenant.persistence.repository;

import com.onlyoffice.tenant.persistence.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {}
