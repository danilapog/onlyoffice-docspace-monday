package com.onlyoffice.user.persistence.repository;

import com.onlyoffice.user.persistence.entity.User;
import com.onlyoffice.user.persistence.entity.UserId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepositoryImpl implements TimeoutUserRepository {
  @PersistenceContext private EntityManager entityManager;

  public Optional<User> findByIdWithTimeout(UserId id, int timeoutInMillis) {
    return entityManager
        .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
        .setParameter("id", id)
        .setHint("jakarta.persistence.query.timeout", timeoutInMillis)
        .getResultList()
        .stream()
        .findFirst();
  }

  public Set<User> findAllByIdsWithTimeout(Set<UserId> ids, int timeoutInMillis) {
    return new HashSet<>(
        entityManager
            .createQuery("SELECT u FROM User u WHERE u.id IN :ids", User.class)
            .setParameter("ids", ids)
            .setHint("jakarta.persistence.query.timeout", timeoutInMillis)
            .getResultList());
  }
}
