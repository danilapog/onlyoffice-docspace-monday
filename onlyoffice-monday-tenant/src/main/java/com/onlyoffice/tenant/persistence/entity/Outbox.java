package com.onlyoffice.tenant.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

// TODO: Replace outbox with CDC in v2?
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "tenants", name = "monday_outbox")
public class Outbox {
  @Id @UuidGenerator private String id;

  @JdbcTypeCode(SqlTypes.JSON)
  private String payload;

  @Enumerated(EnumType.STRING)
  private OutboxType type;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Long createdAt;

  @PrePersist
  protected void prePersist() {
    createdAt = Instant.now().toEpochMilli();
  }
}
