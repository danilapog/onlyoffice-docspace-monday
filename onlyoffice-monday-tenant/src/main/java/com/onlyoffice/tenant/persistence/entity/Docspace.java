package com.onlyoffice.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    schema = "tenants",
    name = "monday_tenant_docspace",
    indexes = {@Index(name = "monday_tenant_docspace_idx", columnList = "url,tenant_id")})
public class Docspace {
  @Id
  @UuidGenerator
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private String id;

  @Column(nullable = false)
  private String url;

  @Column(name = "email", nullable = false)
  private String adminLogin;

  @Column(name = "hash", nullable = false)
  private String adminHash;

  @OneToOne
  @JoinColumn(name = "tenant_id", referencedColumnName = "id")
  private Tenant tenant;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @PrePersist
  protected void prePersist() {
    var now = System.currentTimeMillis();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void preUpdate() {
    updatedAt = System.currentTimeMillis();
  }
}
