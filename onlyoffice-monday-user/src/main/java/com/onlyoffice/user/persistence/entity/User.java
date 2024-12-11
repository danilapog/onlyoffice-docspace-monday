package com.onlyoffice.user.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    schema = "users",
    name = "monday_tenant_users",
    indexes = {
      @Index(name = "monday_tenant_users_idx", columnList = "monday_id,tenant_id"),
      @Index(name = "monday_tenant_docspace_users_idx", columnList = "docspace_id")
    },
    uniqueConstraints = {@UniqueConstraint(columnNames = {"monday_id", "tenant_id"})})
@IdClass(value = UserId.class)
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id
  @Column(name = "monday_id", nullable = false, updatable = false)
  private long mondayId;

  @Id
  @Column(name = "tenant_id", nullable = false, updatable = false)
  private long tenantId;

  @Column(name = "docspace_id", nullable = false)
  private String docSpaceId;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String hash;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Version private long version;
}
