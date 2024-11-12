package com.onlyoffice.tenant.persistence.entity;

public enum OutboxType {
  REFRESH,
  INVITE,
  CREATE_USER_ON_INITIALIZATION,
  REMOVE_TENANT_USERS
}
