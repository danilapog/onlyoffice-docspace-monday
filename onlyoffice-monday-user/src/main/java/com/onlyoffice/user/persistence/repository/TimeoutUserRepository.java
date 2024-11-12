package com.onlyoffice.user.persistence.repository;

import com.onlyoffice.user.persistence.entity.User;
import com.onlyoffice.user.persistence.entity.UserId;
import java.util.Optional;
import java.util.Set;

public interface TimeoutUserRepository {
  Optional<User> findByIdWithTimeout(UserId id, int timeoutInMillis);

  Set<User> findAllByIdsWithTimeout(Set<UserId> ids, int timeoutInMillis);
}
