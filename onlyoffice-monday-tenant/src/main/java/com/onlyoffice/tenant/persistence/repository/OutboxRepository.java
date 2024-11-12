package com.onlyoffice.tenant.persistence.repository;

import com.onlyoffice.tenant.persistence.entity.Outbox;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface OutboxRepository extends JpaRepository<Outbox, String> {
  @QueryHints({
    @QueryHint(name = AvailableSettings.JAKARTA_LOCK_TIMEOUT, value = LockOptions.SKIP_LOCKED + "")
  })
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Outbox> findTop25ByOrderByCreatedAtAsc();
}
